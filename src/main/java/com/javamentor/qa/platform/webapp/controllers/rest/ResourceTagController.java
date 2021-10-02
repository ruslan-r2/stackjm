package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.exception.TagAlreadyExistsException;
import com.javamentor.qa.platform.exception.TagNotFoundException;
import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер тегов", description = "Api для тегов")
@RequestMapping
@AllArgsConstructor
public class ResourceTagController {

    private final TagConverter tagConverter;
    private final IgnoredTagService ignoredTagService;
    private final TagDtoService tagDtoService;

    @Operation(summary = "Возвращает лист содержащий топ-10 тегов")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RelatedTagDto.class)))
    @GetMapping("/api/user/tag/related")
    public ResponseEntity<List<RelatedTagDto>> getTop10Tags() {
        return new ResponseEntity<>(tagDtoService.getTopTags(), HttpStatus.OK);
    }

    @Operation(summary = "добавляет тег в игнорируемые теги")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TagDto.class)))
    @ApiResponse(responseCode = "404", description = "тег с таким id не найден")
    @ApiResponse(responseCode = "400", description = "тег уже был добавлен в игнорируемые ранее")
    @PostMapping("/api/user/tag/{id}/ignored")
    public ResponseEntity<TagDto> addTagToIgnoreTag(@PathVariable(name = "id") Long tagId,
                                                    @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(tagConverter.TagToTagDto(ignoredTagService.add(tagId, user)));
        } catch (TagNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (TagAlreadyExistsException alreadyExistsException) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Возвращает игнорируемые теги пользователя")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IgnoredTagDto.class)))
    @GetMapping("/api/user/tag/ignored")
    public ResponseEntity<List<IgnoredTagDto>> getIgnoreTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(tagDtoService.getIgnoredTagsByUserId(user.getId()));
    }

    @Operation(summary = "Возвращает все отслеживаемые теги авторизированного пользователя")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TrackedTagDto.class))))
    @GetMapping("/api/user/tag/tracked")
    public ResponseEntity<List<TrackedTagDto>> getAllAuthorizedUserTrackedTags() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(tagDtoService.getTrackedByUserId(user.getId()));
    }

}
