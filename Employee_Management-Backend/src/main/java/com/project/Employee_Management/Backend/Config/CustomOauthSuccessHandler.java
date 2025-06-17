package com.project.Employee_Management.Backend.Config;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Service.CustomOAuthUserPrincipal;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component

public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    jwtUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {
    try{

        System.out.println(">>> CustomOauthSuccessHandler hit!");
        CustomOAuthUserPrincipal principal = (CustomOAuthUserPrincipal) authentication.getPrincipal();

        User user = principal.getUser(); // full user entity

        System.out.println("--------------------------------------------------------");
        System.out.println(user);

        // Generate JWT
        String token = jwtUtil.generateToken(user);

        // Set token in cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);

    }
    catch (Exception e) {
        e.printStackTrace();  // Log the issue
        response.sendRedirect("/Employee_dashboard.html");
    }

//        response.sendRedirect("/Employee_dashboard.html");

//        CustomOAuthUserPrincipal principal = (CustomOAuthUserPrincipal) authentication.getPrincipal();
//        String email = principal.getUser().getEmail();
//        System.out.println("Login successful for: " + email);
//
//        response.sendRedirect("/Employee_dashboard.html");
    }

}
