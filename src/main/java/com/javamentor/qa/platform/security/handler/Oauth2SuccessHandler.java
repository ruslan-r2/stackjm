package com.javamentor.qa.platform.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtUtils;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final TokenResponseDTO tokenResponseDTO;
    private final ObjectMapper objectMapper;
    @Value("${defaultOauthPass}")
    private String defaultOauthPass;


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        String oauthEmail;
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> userAttributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();

        if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
            User user = userService.getByEmail(userAttributes.get("email").toString()).orElse(null);
            oauthEmail = userAttributes.get("email").toString();
            if (user == null) {
                User oauthUser = new User();
                oauthUser.setEmail(oauthEmail);
                oauthUser.setFullName(userAttributes.get("name").toString());
                oauthUser.setRole(new Role("ROLE_USER"));
                oauthUser.setPassword(passwordEncoder().encode(defaultOauthPass));
                tokenResponseDTO.setRole(oauthUser.getRole().getName());
                userService.persist(oauthUser);
                System.out.println("Пользователь сохранен");
            }

        } else {
            oauthEmail = userAttributes.get("id") + "@stackjm.ru";
            User user = userService.getByEmail(oauthEmail).orElse(null);
            if (user == null) {
                User oauthUser = new User();
                oauthUser.setEmail(oauthEmail);
                oauthUser.setRole(new Role("ROLE_USER"));
                oauthUser.setPassword(passwordEncoder().encode(defaultOauthPass));
                userService.persist(oauthUser);
                tokenResponseDTO.setRole(oauthUser.getRole().getName());
                System.out.println("Пользователь сохранен");
            }
        }

        String token = jwtUtils.generateJwtTokenOauth(oauthEmail);
        tokenResponseDTO.setToken(token);

        Cookie sessionCookie = new Cookie("StackJM", token);
        httpServletResponse.setHeader("OauthToken", objectMapper.writeValueAsString(tokenResponseDTO));
        httpServletResponse.addCookie(sessionCookie);

        httpServletResponse.sendRedirect("/main");
    }
}
