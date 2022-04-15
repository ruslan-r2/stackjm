package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(name="RelatedTagDto", description = "DTO возвращающая тэги")
public class RelatedTagDto {
    Long id;
    String title;
    Long countQuestion;
}
