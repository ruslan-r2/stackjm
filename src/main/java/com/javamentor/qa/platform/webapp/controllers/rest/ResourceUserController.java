package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class ResourceUserController {

    private UserDtoService userDtoService;

    public ResourceUserController(UserDtoService userDtoService) {
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
        return user.map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new UserDto(),HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Возвращает страницу пользователей")
    @ApiResponse(responseCode = "200", description = "успешное выполнение",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageDto.class)))
    @GetMapping()
    public ResponseEntity<PageDto<UserDto>> getPage(@Parameter(description = "номер страницы", required = true) @RequestParam(value = "currentPage", required = true) Integer currentPage, @Parameter(description = "кол-во пользователей на странице", required = true) @RequestParam(value = "itemsOnPage", required = true) Integer itemsOnPage) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", currentPage);
        parameters.put("itemsOnPage", itemsOnPage);
        parameters.put("workPagination", "allUsers");
        PageDto<UserDto> pageDto = userDtoService.getPageDto(parameters);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @Operation(summary = "Возвращает страницу пользователей, отсортированных по репутации")
    @ApiResponse(responseCode = "200", description = "успешное выполнение",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageDto.class)))
    @GetMapping(value = "/reputation")
    public ResponseEntity<PageDto<UserDto>> getPageWhereUserSortedByReputation(@RequestParam(value = "page", required = true) Integer numberPage, @RequestParam(value = "items", required = false, defaultValue = "10") Integer countItemsOnPage){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", numberPage);
        parameters.put("itemsOnPage", countItemsOnPage);
        parameters.put("sorted-reputation", true);
        parameters.put("workPagination", "allUsers");
        PageDto<UserDto> pageDto = userDtoService.getPageDto(parameters);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @Operation(summary = "Возвращает страницу пользователей, отсортированных по дате регистрации")
    @ApiResponse(responseCode = "200", description = "успешное выполнение",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageDto.class)))
    @GetMapping(value = "/new")
    public ResponseEntity<PageDto<UserDto>> getPageWhereUserSortedByRegistrationDate(@RequestParam(value = "page", required = true) Integer numberPage, @RequestParam(value = "items", required = false, defaultValue = "10") Integer countItemsOnPage) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", numberPage);
        parameters.put("itemsOnPage", countItemsOnPage);
        parameters.put("sorted-registrationDate", true);
        parameters.put("workPagination", "allUsers");
        PageDto<UserDto> pageDto = userDtoService.getPageDto(parameters);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @Operation(summary = "Возвращает страницу пользователей, отсортированных по количеству голосов")
    @ApiResponse(responseCode = "200", description = "успешное выполнение",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageDto.class)))
    @GetMapping(value = "/vote")
    public ResponseEntity<PageDto<UserDto>> getPageWhereUserSortedByVotes(@RequestParam(value = "page", required = true) Integer numberPage, @RequestParam(value = "items", required = false, defaultValue = "10") Integer countItemsOnPage){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", numberPage);
        parameters.put("itemsOnPage", countItemsOnPage);
        parameters.put("sorted-votes", true);
        parameters.put("workPagination", "allUsers");
        PageDto<UserDto> pageDto = userDtoService.getPageDto(parameters);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
