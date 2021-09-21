package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "вопрос")
public class QuestionDto {
    @Parameter(description = "id вопроса")
    private Long id;
    @Schema(description = "Заголовок вопроса")
    private String title;
    @Schema(description = "id автора вопроса")
    private Long authorId;
    @Schema(description = "имя автора вопроса")
    private String authorName;
    @Schema(description = "картинка автора")
    private String authorImage;
    @Schema(description = "репутация автора")
    private Long authorReputation;
    @Schema(description = "вопрос")
    private String description;
    @Schema(description = "количество просмотров")
    private Long viewCount;
    @Schema(description = "количество ответов")
    private Long countAnswer;
    @Schema(description = "рейтинг вопроса")
    private Long countValuable;
    @Schema(description = "дата создания вопроса")
    private LocalDateTime persistDateTime;
    @Schema(description = "дата обновления вопроса")
    private LocalDateTime lastUpdateDateTime;
    @Schema(description = "список тэгов вопроса")
    private List<TagDto> listTagDto;
}
