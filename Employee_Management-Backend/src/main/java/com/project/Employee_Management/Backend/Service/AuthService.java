package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.dto.LoginRequest;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.UserRepository;
//import com.project.Employee_Management.Backend.Config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    // private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository /*, JwtUtil jwtUtil */) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        // this.jwtUtil = jwtUtil;
    }

    public String authenticateUser(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid username/password"); // or custom exception
        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        User user = userRepository.findByUsername(loginRequest.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Generate JWT token
        return "Success";
       // return jwtUtil.generateToken(user);
    }
}
