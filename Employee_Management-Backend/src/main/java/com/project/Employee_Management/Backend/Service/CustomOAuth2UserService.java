package com.project.Employee_Management.Backend.Service;

import com.project.Employee_Management.Backend.Model.User;
import com.project.Employee_Management.Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        System.out.println(">>> CustomOAuth2UserService hit!");
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String email = oAuth2User.getAttribute("email");
        System.out.println("-------------------------------------------------------");
        System.out.println(email);

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from provider");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException("User not found: " + email));

        return new CustomOAuthUserPrincipal(user, oAuth2User.getAttributes());
    }

}




