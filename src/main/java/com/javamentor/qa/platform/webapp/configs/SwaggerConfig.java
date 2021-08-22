package com.javamentor.qa.platform.webapp.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                                .title("StackJM")
                                .version(appVersion)
                                .description(appDesciption)
                                .termsOfService("http://swagger.io/terms/")
                                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .info(new Info()
                                .contact(
                                        new Contact()
                                                .email("yozhikov.dev@gmail.com")
                                                .name("Ezhikov Daniil")
                                ));

    }
}

