package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Schema(description = "DTO описывающая запрос на аутентификацию")
@Validated
public class AuthenticationRequestDTO {

    @NotEmpty
    private String login;

    @NotEmpty
    private String pass;

}
