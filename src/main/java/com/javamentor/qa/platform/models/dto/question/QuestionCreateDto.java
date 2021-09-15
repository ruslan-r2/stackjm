package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dto создания вопроса")
public class QuestionCreateDto {

    @Schema(description = "Заголовок вопроса")
    private String title;
    @Schema(description = "Описание вопроса")
    private String description;
    @Schema(description = "Список тегов вопроса")
    private List<TagDto> tags;
}
