package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ResourceUserControllerTest extends AbstractIntegrationTest {
    private String username;
    private String password;

    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_status_not_found() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user/99").header("Authorization", getToken(username, password)))
                .andExpect(status().isNotFound());
    }


    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_get_user_by_id() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user/100").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(100)))
                .andExpect(jsonPath("$.email", equalTo("admin@mail.ru")))
                .andExpect(jsonPath("$.reputation", equalTo(15)));
    }
}
