package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "регистрация пользователя")
public class UserRegistrationDto {

    @Parameter(
            name = "firstName",
            example = "John",
            required = true)
    @NotEmpty
    private String firstName;

    @Parameter(
            name = "lastName",
            example = "Smith",
            required = true)
    @NotEmpty
    private String lastName;

    @Parameter(
            name = "email",
            example = "email@mail.ru",
            required = true)
    @NotEmpty
    private String email;

    @Parameter(
            name = "password",
            example = "1234",
            required = true)
    @NotEmpty
    private String password;

}
