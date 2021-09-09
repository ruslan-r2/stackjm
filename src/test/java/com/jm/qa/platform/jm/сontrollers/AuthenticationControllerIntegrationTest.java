package com.jm.qa.platform.jm.сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthenticationControllerIntegrationTest extends AbstractIntegrationTest {

    private String URL = "/api/auth/token";

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void authenticationControllerCheck_CORRECT() throws Exception {
        AuthenticationRequestDTO correct = new AuthenticationRequestDTO("admin@admin.com","admin");
        String json = objectMapper.writeValueAsString(correct);
        this.mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void authenticationControllerCheck_INCORRECT() throws Exception {
        AuthenticationRequestDTO incorrect = new AuthenticationRequestDTO("admin","admin");
        String json = objectMapper.writeValueAsString(incorrect);
        this.mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
