package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class UserResourceController {

    private UserDtoService userDtoService;

    public UserResourceController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @Operation(summary = "Возвращает пользователя по его id")
    @ApiResponse(responseCode = "200", description = "успешное выполнение",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", description = "пользователь не найден")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserDtoById(@Parameter(description = "id по которому нужно найти пользователя", required = true) @PathVariable(value = "id", required = true) Long id) {
        Optional<UserDto> user = userDtoService.getById(id);
        return user.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
