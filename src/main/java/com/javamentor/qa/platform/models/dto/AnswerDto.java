package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
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
@Schema(description = "ответ")
public class AnswerDto {
    @Schema(description = "id ответа на вопрос")
    private Long id;
    @Schema(description = "id пользователя")
    private Long userId;
    @Parameter(description = "id вопроса")
    private Long questionId;
    @Schema(description = "текст ответа")
    private String body;
    @Schema(description = "дата создания ответа")
    private LocalDateTime persistDate;
    @Schema(description = "польза ответа")
    private Boolean isHelpful;
    @Schema(description = "дата решения вопроса")
    private LocalDateTime dateAccept;
    @Schema(description = "рейтинг ответа")
    private Long countValuable;
    @Schema(description = "ссылка на картинку пользователя")
    private String image;
    @Schema(description = "никнейм пользователя")
    private String nickname;
}
