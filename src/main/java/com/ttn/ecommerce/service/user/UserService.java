package com.ttn.ecommerce.service.user;

import com.ttn.ecommerce.dto.LoginDto;
import com.ttn.ecommerce.dto.PasswordDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> login(LoginDto loginDto);

    ResponseEntity<?> logout(HttpServletRequest httpServletRequest);

    ResponseEntity<String> forgotPassword(String email);

    void sendResetEmail(String email, String token);

    ResponseEntity<String> reset(String resetToken, PasswordDto passwordDto);
}
