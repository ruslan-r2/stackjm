package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    test controller for example
*/

@RestController
@RequestMapping("/rest/api/test")
public class HelloController {

    @GetMapping
    public String test(){
        return "Hello";
    }

}
