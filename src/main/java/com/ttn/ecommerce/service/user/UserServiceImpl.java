package com.ttn.ecommerce.service.user;

import com.ttn.ecommerce.dto.LoginDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Token;
import com.ttn.ecommerce.model.Users;
import com.ttn.ecommerce.repository.TokenRepository;
import com.ttn.ecommerce.repository.UserRepository;
import com.ttn.ecommerce.service.EmailSenderService;
import com.ttn.ecommerce.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    PasswordEncoder passwordEncoder;
    int invalidAttemptCount = 0;
    @Autowired
    private TokenRepository tokenRepository;


    @Value("${url}")
    private String url;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        String token = "empty";

        Users user = userRepository.findByEmail(loginDto.getEmail());
        if (Objects.isNull(user)) {
            throw new GenericException("Invalid email", HttpStatus.NOT_FOUND);
        }
        if (!user.getIsActive()) {
            throw new GenericException("Your account is not activated.", HttpStatus.UNAUTHORIZED);
        }
        if (user.getIsLocked()) {
            throw new GenericException("Your account is locked. Please contact admin to unlock your account.", HttpStatus.FORBIDDEN);
        }


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Token activeToken = tokenRepository.findByEmail(user.getEmail());

            if (!Objects.isNull(activeToken) && activeToken.getIsActive())
                if (jwtUtils.isTokenExpired(activeToken.getJwt())) {
                    tokenRepository.delete(activeToken);
                } else {
                    throw new GenericException("You already have an active session. \n" + activeToken.getJwt(), HttpStatus.OK);
                }else {
                token = jwtUtils.generateToken(user.getEmail(), 1440);
                Token newToken = new Token();
                newToken.setEmail(user.getEmail());
                newToken.setJwt(token);
                tokenRepository.save(newToken);
            }

        } catch (BadCredentialsException e) {

            invalidAttemptCount++;

            if (invalidAttemptCount >= 3) {
                user.setIsLocked(true);
                user.setIsActive(false);
                user.setInvalidAttemptCount(invalidAttemptCount);
                userRepository.save(user);
                throw new GenericException("Your account has been locked due to too many failed login attempts.", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password.");
        }

        return new ResponseEntity<>("User logged in successfully : \n" + token, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        String jwt = jwtUtils.getTokenThroughRequest(httpServletRequest);
        try {
            String email = jwtUtils.validateTokenAndRetrieveSubject(jwt);
            Token token = tokenRepository.findByEmailAndJwt(email, jwt);
            if (token == null) {
                throw new GenericException("Token not found", HttpStatus.NOT_FOUND);
            }
            tokenRepository.delete(token);
            return ResponseEntity.status(HttpStatus.OK).body("logout");
        } catch (JwtException e) {
            throw new GenericException("Invalid token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new GenericException("Error occurred while logging out", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> forgotPassword(String email) {
        Users user = userRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new GenericException("Email address not found.", HttpStatus.NOT_FOUND);
        } else if (!user.getIsActive()) {
            throw new GenericException("User is not active. Please contact support for assistance.", HttpStatus.FORBIDDEN);
        }
        String token = jwtUtils.generateToken(user.getEmail(), 15);
        sendResetEmail(user.getEmail(), token);
        return ResponseEntity.ok().body("Reset password link sent successfully.");
    }

    @Override
    public void sendResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Forgot Password");
        message.setText("Please use the following token to activate: " +
                url + "/user/reset/password?token=" + token);
        emailSenderService.sendEmail(message);
    }

    @Override
    public ResponseEntity<String> reset(String resetToken, PasswordDto passwordDto) {
        String email = jwtUtils.validateTokenAndRetrieveSubject(resetToken);
        Users user = userRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new GenericException("Email address not found.", HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(passwordDto.getPassword())) {
            throw new GenericException("Password cannot be blank.", HttpStatus.BAD_REQUEST);
        }
        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
            throw new GenericException("Password and confirm password do not match.", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully.");
    }

}
