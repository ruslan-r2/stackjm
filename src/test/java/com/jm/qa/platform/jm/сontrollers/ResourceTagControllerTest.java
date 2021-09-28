package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceTagControllerTest extends AbstractIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

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
        Long id = 101L;

        mockMvc.perform(get("/api/user/tag/{id}/ignored", id)
                .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(101)))
                .andExpect(jsonPath("name", is("Spring")))
                .andReturn();

        IgnoredTag ignoredTag = (IgnoredTag) entityManager.createQuery("SELECT it FROM IgnoredTag it JOIN FETCH it.ignoredTag WHERE it.id=1").getSingleResult();
        assertThat(ignoredTag).isNotNull();
        assertThat(ignoredTag.getIgnoredTag().getName()).isEqualTo("Spring");

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

    @Test
    @DataSet(value = {"topTagController/tags.yml", "topTagController/users.yml", "topTagController/ignored_tags.yml",
            "topTagController/roles.yml"}, cleanBefore = true, cleanAfter = true)
    public void getIgnoreTags() throws Exception {

        mockMvc.perform(get("/api/user/tag/ignored")
                        .header("Authorization", getToken("user@mail.ru", "user"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Hibernate")))
                .andExpect(jsonPath("$[1].name", is("Spring")));

        mockMvc.perform(get("/api/user/tag/ignored")
                        .header("Authorization", getToken("ruslan@mail.ru", "user1"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("ООП")));
    }

    @Test
    @DataSet(value = {"topTagController/tags.yml", "topTagController/users.yml", "topTagController/tracked_tags.yml",
            "topTagController/roles.yml"}, cleanBefore = true, cleanAfter = true)
    public void getAuthorizedUserTrackedTags() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        mockMvc.perform(get("/api/user/tag/tracked")
                        .header("Authorization", getToken(username, password))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Hibernate")))
                .andExpect(jsonPath("$[1].name", is("Spring")))
                .andExpect(jsonPath("$[2].name", is("ООП")));
    }

}
