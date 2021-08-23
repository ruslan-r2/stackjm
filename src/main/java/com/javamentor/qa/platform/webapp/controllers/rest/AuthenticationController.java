package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.security.UserDetailsServiceImpl;
import com.javamentor.qa.platform.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Tag(name = "Контроллер Аутентификации", description = "Rest контроллер предоставляющий API для авторизации пользователя через JWT")
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenResponseDTO tokenResponseDTO;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider, TokenResponseDTO tokenResponseDTO,UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenResponseDTO = tokenResponseDTO;
        this.userDetailsService = userDetailsService;
    }


    @Operation(summary = "Метод аутентификации", description = "При успешной аутентификации метод возвращает JWT")
    @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешна, токен сгенерирован",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Аутентификация не успешна, проверьте валидность данных для входа")
    @ApiResponse(responseCode = "500", description = "Данные с формы не прошли валидацию")
    @PostMapping("/api/auth/token")
    public ResponseEntity<?> authentication(@Parameter(schema = @Schema(implementation = AuthenticationRequestDTO.class))
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для аутентификации", required = true,
                                                    content = @Content(
                                                            schema = @Schema(implementation = AuthenticationRequestDTO.class))) @Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(),
                    authenticationRequest.getPass()));

            String token = jwtTokenProvider.createToken(authenticationRequest.getLogin(), userDetailsService.getFindUser().get().getRole().getName());
            tokenResponseDTO.setRole(userDetailsService.getFindUser().get().getRole().getName());
            tokenResponseDTO.setToken(token);

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

}
