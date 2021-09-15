package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dto Вопроса")
public class QuestionDto {

    @Parameter(description = "Id Вопроса")
    private Long id;
    @Schema(description = "Заголовок вопроса")
    private String title;
    @Schema(description = "Id автора вопроса")
    private Long authorId;
    @Schema(description = "Имя автора вопроса")
    private String authorName;
    @Schema(description = "Аватар автора вопроса")
    private String authorImage;
    @Schema(description = "Описание вопроса")
    private String description;
    @Schema(description = "Рейтинг вопроса")
    private Long authorReputation;
    @Schema(description = "Количество ответов")
    private int countAnswer;
    @Schema(description = "Количество отметок 'Полезное'")
    private int countValuable;
    @Schema(description = "Дата и время размещения вопроса")
    private LocalDateTime persistDateTime;
    @Schema(description = "Дата и время последнего обновления вопроса")
    private LocalDateTime lastUpdateDateTime;
    @Schema(description = "Список тегов вопроса")
    private List<TagDto> listTagDto;
}
