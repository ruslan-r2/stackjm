package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestUserResourceController extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;


    @WithMockUser("spring")
    @Test
    @DataSet(value = {"userResourceController/users.yml",
            "userResourceController/roles.yml",
            "userResourceController/reputations.yml",
            "userResourceController/answers.yml",
            "userResourceController/questions.yml"},cleanAfter = true,cleanBefore = true)
    public void should_return_status_not_found() throws Exception {
        mockMvc.perform(get("/api/user/99"))
                .andExpect(status().isNotFound());
    }


    @WithMockUser("spring")
    @Test
    @DataSet(value = {"userResourceController/users.yml",
            "userResourceController/roles.yml",
            "userResourceController/reputations.yml",
            "userResourceController/answers.yml",
            "userResourceController/questions.yml"},cleanAfter = true,cleanBefore = true)
    public void should_get_user_by_id() throws Exception {
        mockMvc.perform(get("/api/user/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(100)))
                .andExpect(jsonPath("$.email", equalTo("admin@mail.ru")))
                .andExpect(jsonPath("$.reputation", equalTo(15)));
    }
}
