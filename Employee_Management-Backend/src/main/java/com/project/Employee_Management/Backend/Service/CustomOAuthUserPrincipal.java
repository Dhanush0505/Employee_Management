package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.Model.Enum.role;
import com.project.Employee_Management.Backend.Model.User;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuthUserPrincipal implements OAuth2User {

    @Getter
    private User user;
    private final Map<String, Object> attributes;

    public CustomOAuthUserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public String getEmail() {
        return user.getEmail(); // email
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
