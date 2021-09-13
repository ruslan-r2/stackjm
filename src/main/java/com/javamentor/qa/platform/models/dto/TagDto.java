package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    @Schema(description = "id тега")
    private Long id;

    @Schema(description = "тег")
    private Tag ignoredTag;

    @Schema(description = "юзер")
    private User user;

}
