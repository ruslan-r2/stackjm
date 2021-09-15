package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dto тега")
public class TagDto {

    @Parameter(description = "Id тега")
    private Long id;
    @Schema(description = "Имя тега")
    private String name;
    @Schema(description = "Описание тега")
    private String description;
}
