package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceTagController {

    @Autowired
    TagService tagService;


     @PostMapping("/api/user/tag/{id}/ignored")
    public ResponseEntity<TagDto> addTagToIgnoredTag(@PathVariable @NonNull Long id, Principal principal) {
         if(tagService.existsById(id)){


             Tag tag = tagService.getById(id).get();

         }
         System.out.println(principal.getName());


         return null;
     }

}
