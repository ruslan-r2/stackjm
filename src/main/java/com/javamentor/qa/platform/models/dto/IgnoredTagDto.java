package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="IgnoredTagDto", description = "DTO возвращающая игнорируемые тэги")
public class IgnoredTagDto {
    @Schema(description = "id тэга")
    private Long id;
    @Schema(description = "название тэга")
    private String name;
}