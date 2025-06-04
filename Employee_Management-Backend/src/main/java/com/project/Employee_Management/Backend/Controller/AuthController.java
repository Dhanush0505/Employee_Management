//package com.project.Employee_Management.Backend.Controller;
//
//import com.project.Employee_Management.Backend.dto.LoginRequest;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        // AuthService handles authentication and returns JWT token or error
//        String token = authService.authenticateUser(loginRequest);
//        return ResponseEntity.ok().body(token);
//    }
//}
