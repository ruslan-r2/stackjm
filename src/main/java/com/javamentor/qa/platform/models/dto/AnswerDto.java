package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность ответа")
public class AnswerDto {

    @Schema(description = "Идентификатор ответа")
    private Long id;
    @Schema(description = "Идентификатор пользователя")
    private Long userId;
    @Schema(description = "Идентификатор вопроса")
    private Long questionId;
    @Schema(description = "Тело ответа")
    private String body;
    @Schema(description = "Идентификатор пользователя")
    private LocalDateTime persistDate;
    @Schema(description = "Идентификатор пользователя")
    private Boolean isHelpful;
    @Schema(description = "Идентификатор пользователя")
    private LocalDateTime dateAccept;
    @Schema(description = "Идентификатор пользователя")
    private Long countValuable;
    @Schema(description = "Идентификатор пользователя")
    private String image;
    @Schema(description = "Никнейм")
    private String nickName;
}
