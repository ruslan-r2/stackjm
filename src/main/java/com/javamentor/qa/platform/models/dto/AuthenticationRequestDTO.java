package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Schema(name="AuthenticationRequestDTO", description = "DTO описывающая запрос на аутентификацию, данный параметр не должен быть пустым")
public class AuthenticationRequestDTO {

    @Parameter(
            name = "login",
            example = "admin@gmail.com",
            required = true)
    @NotBlank
    private String login;

    @Parameter(
            name = "pass",
            example = "admin",
            required = true)
    @NotBlank
    private String pass;

}
