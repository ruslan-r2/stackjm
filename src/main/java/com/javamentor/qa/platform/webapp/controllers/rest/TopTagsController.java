package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер топ-10 тегов", description = "Api для вывода топ-10 тегов")
@RequestMapping("/api/user/tag/related")
public class TopTagsController {

    private final TagService tagService;

    private final TagConverter tagConverter;

    public TopTagsController(TagService tagService, TagConverter tagConverter) {
        this.tagService = tagService;
        this.tagConverter = tagConverter;
    }

    @Operation(summary = "Возвращает лист содержащий топ-10 тегов")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RelatedTagDto.class)))
    @GetMapping
    public ResponseEntity<ArrayList<RelatedTagDto>> getTop10Tags() {
        ArrayList<RelatedTagDto> list = new ArrayList<>();
        for (com.javamentor.qa.platform.models.entity.question.Tag tag :
                tagService.getAll()) {
            list.add(tagConverter.tagToRelatedTagDto(tag));
        }
        list.sort((o1, o2) -> (int) (o2.getCountQuestion() - o1.getCountQuestion()));
        if (list.size() > 10) {
            list = (ArrayList<RelatedTagDto>) list.subList(0, 10);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
