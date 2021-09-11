package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "пользователь")
public class UserDto {
    @Parameter(description = "id пользователя")
    private Long id;
    @Schema(description = "почта пользователя")
    private String email;
    @Schema(description = "имя пользователя")
    private String fullName;
    @Schema(description = "ссылка на изображение пользователя")
    private String linkImage;
    @Schema(description = "город пользователя")
    private String city;
    @Schema(description = "репутация пользователя")
    private int reputation;
}
