package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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

    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/sorted_reputation/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_users_with_id_one_hundred_one_and_one_hundred_where_sorted_reputation() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user/reputation?page=1&items=2").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(2)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(3)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(2)))
                .andExpect(jsonPath("$.items.length()", equalTo(2)))
                .andExpect(jsonPath("$.items[0].id", equalTo(101)))
                .andExpect(jsonPath("$.items[0].reputation", equalTo(20)))
                .andExpect(jsonPath("$.items[1].id", equalTo(100)))
                .andExpect(jsonPath("$.items[1].reputation", equalTo(15)));
    }

    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/sorted_reputation/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_user_with_id_one_hundred_two_where_sorted_reputation() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user/reputation?page=2&items=2").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(2)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(2)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(3)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(2)))
                .andExpect(jsonPath("$.items.length()", equalTo(1)))
                .andExpect(jsonPath("$.items[0].id", equalTo(102)))
                .andExpect(jsonPath("$.items[0].reputation", equalTo(5)));
    }

    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/sorted_reputation/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_users_where_sorted_reputation_without_parameter_items() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user/reputation?page=1").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(1)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(3)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(10)))
                .andExpect(jsonPath("$.items.length()", equalTo(3)))
                .andExpect(jsonPath("$.items[0].id", equalTo(101)))
                .andExpect(jsonPath("$.items[0].reputation", equalTo(20)))
                .andExpect(jsonPath("$.items[1].id", equalTo(100)))
                .andExpect(jsonPath("$.items[1].reputation", equalTo(15)))
                .andExpect(jsonPath("$.items[2].id", equalTo(102)))
                .andExpect(jsonPath("$.items[2].reputation", equalTo(5)));
    }


    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_users_with_id_one_hundred_and_one_hundred_one() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user?currentPage=1&itemsOnPage=2").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(2)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(3)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(2)))
                .andExpect(jsonPath("$.items.length()", equalTo(2)))
                .andExpect(jsonPath("$.items[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].reputation", equalTo(15)))
                .andExpect(jsonPath("$.items[1].id", equalTo(101)))
                .andExpect(jsonPath("$.items[1].reputation", equalTo(0)));
    }

    @Test
    @DataSet(value = {"resource_user_controller/users.yml",
            "resource_user_controller/roles.yml",
            "resource_user_controller/reputations.yml",
            "resource_user_controller/answers.yml",
            "resource_user_controller/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_user_with_id_one_hundred_two() throws Exception {
        username = "user@mail.ru";
        password = "user";
        mockMvc.perform(get("/api/user?currentPage=2&itemsOnPage=2").header("Authorization", getToken(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(2)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(2)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(3)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(2)))
                .andExpect(jsonPath("$.items.length()", equalTo(1)))
                .andExpect(jsonPath("$.items[0].id", equalTo(102)))
                .andExpect(jsonPath("$.items[0].reputation", equalTo(0)));
    }
}
