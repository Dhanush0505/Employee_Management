package com.project.Employee_Management.Backend.Controller;

import com.project.Employee_Management.Backend.Bcryptencoder;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.UserRepository;
import com.project.Employee_Management.Backend.Service.OTPService;
import com.project.Employee_Management.Backend.dto.AuthResponse;
import com.project.Employee_Management.Backend.Service.AuthService;
import com.project.Employee_Management.Backend.dto.LoginRequest;
import com.project.Employee_Management.Backend.dto.OtpRequest;
import com.project.Employee_Management.Backend.dto.ResetPasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final OTPService otpService;
    public AuthController(AuthService authService, UserRepository userRepository, OTPService otpService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.otpService = otpService;
    }
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("Email not found in the system");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/otp-handler")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());

        if (isValid) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }

}


