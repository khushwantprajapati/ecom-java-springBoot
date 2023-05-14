package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.LoginDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        return userServiceImpl.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        return userServiceImpl.logout(httpServletRequest);
    }

    @PostMapping("/forgot/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        return userServiceImpl.forgotPassword(email);
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> validateResetToken(@RequestParam String token,@Valid @RequestBody PasswordDto passwordDto) {
        return userServiceImpl.reset(token, passwordDto);
    }
}




