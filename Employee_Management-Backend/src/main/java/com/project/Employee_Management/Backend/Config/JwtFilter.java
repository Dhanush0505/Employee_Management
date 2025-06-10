package com.project.Employee_Management.Backend.Config;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    jwtUtil jwtUtil;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Send 401
                response.getWriter().write("Invalid or expired JWT token");
                return; // Stop filter chain
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userdata = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

            if(jwtUtil.ValidateToken(token, userdata)){
                UsernamePasswordAuthenticationToken authenticatedtoken =
                        new UsernamePasswordAuthenticationToken(userdata, null, userdata.getAuthorities());

                authenticatedtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticatedtoken);

            }
        }
        filterChain.doFilter(request, response);
    }
}
