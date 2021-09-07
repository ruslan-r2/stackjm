package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Tag(name = "Контроллер Аутентификации", description = "Rest контроллер предоставляющий API для авторизации пользователя через JWT")
@RestController @RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenResponseDTO tokenResponseDTO;

    @Operation(summary = "Метод аутентификации", description = "При успешной аутентификации метод возвращает JWT")
    @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешна, токен сгенерирован",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Аутентификация не успешна, проверьте валидность данных для входа")
    @PostMapping("/api/auth/token")
    public ResponseEntity<?> authentication(@Parameter(schema = @Schema(implementation = AuthenticationRequestDTO.class))
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для аутентификации", required = true,
                                                    content = @Content(
                                                            schema = @Schema(implementation = AuthenticationRequestDTO.class))) @Valid @RequestBody AuthenticationRequestDTO authenticationRequest, HttpServletResponse response) {

        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getPass()));
            User authUser = (User) authenticate.getPrincipal();
            String token = jwtUtils.generateJwtToken(authenticate);
            tokenResponseDTO.setRole(authUser.getRole().getName());
            tokenResponseDTO.setToken(token);
            Cookie sessionCookie = new Cookie("StackJM", token);
            response.addCookie(sessionCookie);
            return ResponseEntity.ok(tokenResponseDTO);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Неверный email или пароль, проверьте валидность данных для входа", HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Логаут, пока просто шаблон")
    @PostMapping("/api/auth/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    }
}
