package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        mockMvc.perform(post("/api/user/tag/{id}/ignored", id)
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
        mockMvc.perform(post("/api/user/tag/999/ignored")
                .header("Authorization", getToken(username, password)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"topTagController/tags.yml", "topTagController/users.yml", "topTagController/roles.yml"},
            cleanBefore = true, cleanAfter = true)
    public void addAlreadyAddedTagToIgnoredTag() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        Long id = 101L;

        mockMvc.perform(post("/api/user/tag/{id}/ignored", id)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(101)))
                .andExpect(jsonPath("name", is("Spring")));

        mockMvc.perform(post("/api/user/tag/{id}/ignored", id)
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

    @Test
    @DataSet(value = {"resource_tag_controller/addTagToTracked.yml"},
             cleanBefore = true, cleanAfter = true)
    public void addExistingTagToTracked() throws Exception {
        String username = "user@mail.ru";
        String password = "user";
        String addTagToTrackedUrl = "/api/user/tag/{id}/tracked";

        mockMvc.perform(post(addTagToTrackedUrl, 102).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id", is(102)));


        List<TrackedTag> trackedTag = entityManager.createQuery("select t from TrackedTag t where t.trackedTag.id = :trackedTagId").
                setParameter("trackedTagId", 102L).getResultList();
        assertFalse(Collections.isEmpty(trackedTag));
    }

    @Test
    @DataSet(value = "resource_tag_controller/addTagToTracked.yml")
    public void addNotExistingTagToTracked() throws Exception {
        String addTagToTrackedUrl = "/api/user/tag/{id}/tracked";

        List<TrackedTag> trackedTag = entityManager.createQuery("select t from TrackedTag t where t.trackedTag.id = :trackedTagId").
                setParameter("trackedTagId", 999L).getResultList();
        assertTrue(Collections.isEmpty(trackedTag));

        String username = "user@mail.ru";
        String password = "user";
        mockMvc.perform(post(addTagToTrackedUrl, 999).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isBadRequest());
    }
}
