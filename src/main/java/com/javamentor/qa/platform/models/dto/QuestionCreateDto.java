package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO создания нового вопроса")
public class QuestionCreateDto {

    @NotNull
    @Schema(description = "Заголовок созадваемого вопроса")
    private String title;
    @NotNull
    @Schema(description = "Описание создаваемого вопроса")
    private String description;
    @NotNull
    @Schema(description = "Теги создаваемого вопроса")
    private List<TagDto> tags;
}
