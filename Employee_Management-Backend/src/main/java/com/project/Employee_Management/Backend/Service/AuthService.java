package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.Config.jwtUtil;
import com.project.Employee_Management.Backend.dto.AuthResponse;
import com.project.Employee_Management.Backend.dto.LoginRequest;
import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.UserRepository;
//import com.project.Employee_Management.Backend.Config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final jwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository , jwtUtil jwtUtil ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username/password");
        }

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
         String token = jwtUtil.generateToken(user);

        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}

