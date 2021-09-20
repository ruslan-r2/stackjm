package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceTagControllerTest extends AbstractIntegrationTest {

    @Test
    @DataSet(value = "topTagController/getTopTags.yml", cleanBefore = true, cleanAfter = true)
    public void getTopTags() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        mockMvc.perform(get("/api/user/tag/related")
                .header("Authorization", getToken(username, password))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].countQuestion", is(3)))
                .andExpect(jsonPath("$[1].countQuestion", is(2)))
                .andExpect(jsonPath("$[2].countQuestion", is(1)));
    }

    @Test
    @DataSet(value = {"topTagController/tags.yml", "topTagController/users.yml", "topTagController/roles.yml"},
            cleanBefore = true, cleanAfter = true)
    public void addExistTagToIgnoredTag() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        mockMvc.perform(get("/api/user/tag/101/ignored")
                .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(101)))
                .andExpect(jsonPath("name", is("Spring")));
    }

    @Test
    @DataSet(value = {"topTagController/users.yml", "topTagController/roles.yml"},
            cleanBefore = true, cleanAfter = true)
    public void addNotExistTagToIgnoredTag() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        mockMvc.perform(get("/api/user/tag/999/ignored")
                .header("Authorization", getToken(username, password)))
                .andExpect(status().isBadRequest());
    }

}
