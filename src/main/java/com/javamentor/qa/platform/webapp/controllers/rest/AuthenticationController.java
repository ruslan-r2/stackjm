
/**
 * Rest контроллер, предоставляющий API для авторизации пользователя через JWT.
 * @autor Ежиков Даниил
 * @version 1.0
 */
package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtTokenProvider;
import com.javamentor.qa.platform.service.impl.dto.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider, JwtTokenProvider jwtTokenProvider1) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider1;
    }

    @PostMapping("/api/auth/token")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDTO authenticationRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authenticationRequest.getLogin(),
                            authenticationRequest.getPass()));

            Optional<User> user = userService.getByEmail(authenticationRequest.getLogin());
            String token = jwtTokenProvider.createToken(authenticationRequest.getLogin(), user.get().getRole().getName());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", authenticationRequest.getLogin());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Неверный email или пароль", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/api/auth/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}
