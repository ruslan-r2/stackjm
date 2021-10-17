package com.javamentor.qa.platform.webapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("regpage");
        registry.addViewController("/profile").setViewName("profile");
        registry.addViewController("/question/add").setViewName("question");
        registry.addViewController("/users").setViewName("usersPage");
        registry.addViewController("/questions").setViewName("questions");
        registry.addViewController("/tags").setViewName("tag_page");
        registry.addViewController("/main").setViewName("main");
    }

}