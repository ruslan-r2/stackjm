package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Контроллер тегов", description = "Api для тегов")
@RequestMapping
public class ResourceTagController {

    @Autowired
    private TagDtoDao tagDtoDao;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagConverter tagConverter;

    @Autowired
    private IgnoredTagService ignoredTagService;

    @Operation(summary = "Возвращает лист содержащий топ-10 тегов")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RelatedTagDto.class)))
    @GetMapping("/api/user/tag/related")
    public ResponseEntity<List<RelatedTagDto>> getTop10Tags() {
        return new ResponseEntity<>(tagDtoDao.getTopTags(), HttpStatus.OK);
    }

    @Operation(summary = "добавляет тег в игнорируемые теги")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RelatedTagDto.class)))
    @GetMapping("/api/user/tag/{id}/ignored")
    public ResponseEntity<TagDto> addTagToIgnoreTag(@PathVariable(name = "id") Long tagId, @ApiIgnore Principal principal) {
        if (!tagService.existsById(tagId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Tag tag = tagService.getById(tagId).get();
        User user = userService.getByEmail(principal.getName()).get();
        ignoredTagService.persist(new IgnoredTag(tag, user));
        return new ResponseEntity<>(tagConverter.TagToTagDto(tag), HttpStatus.OK);
    }

}
