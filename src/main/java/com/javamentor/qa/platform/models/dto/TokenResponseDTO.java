package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(name="TokenResponseDTO", description = "DTO возвращающая токен")
public class TokenResponseDTO {

    @Parameter(
            name = "role",
            example = "ROLE_ADMIN",
            required = true)
    @NotEmpty
    private String role;

    @Parameter(
            name = "token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJyb2xlIjoiUk9MRV9BRE1JT",
            required = true)
    @NotEmpty
    private String token;

}
