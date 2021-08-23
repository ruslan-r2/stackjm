package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Schema(name="AuthenticationRequestDTO", description = "DTO описывающая запрос на аутентификацию, данная DTO не должна быть пустая")
@Validated
public class AuthenticationRequestDTO {

    @Parameter(
            name = "login",
            example = "admin@gmail.com",
            required = true)
    @NotEmpty
    private String login;

    @Parameter(
            name = "pass",
            example = "admin",
            required = true)
    @NotEmpty
    private String pass;

}
