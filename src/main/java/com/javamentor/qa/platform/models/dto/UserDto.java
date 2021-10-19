package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long reputation;
    @Schema(description = "дата регистрации пользователя")
    private LocalDateTime registrationDate;
    @Schema(description = "количество голосов пользователя")
    private Long votes;
    @Schema(description = "список топ-3 тэгов пользователя")
    private List<TagDto> listTop3TagDto;
}
