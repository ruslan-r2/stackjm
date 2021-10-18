package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import io.jsonwebtoken.lang.Collections;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ResourceQuestionControllerTest extends AbstractIntegrationTest {
    private String username;
    private String password;
    private String token;
    private String resourceQuestionControllerUrl = "/api/user/question";

    @PersistenceContext
    private EntityManager entityManager;

    private long getReputationByIdUser(Long idUser) {
        return entityManager.createQuery("SELECT COALESCE(SUM(r.count), 0) FROM Reputation r WHERE r.author.id = :id", Long.class)
                .setParameter("id", idUser)
                .getSingleResult();
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/question_views.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getById() throws Exception {
        int correctId = 100;
        int incorrectId = -100;
        username = "user@mail.ru";
        password = "user";
        String URL = "/api/user/question/{id}";

        //Существующий ID вопроса
        mockMvc.perform(get(URL, correctId).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.title", is("title0")))
                .andExpect(jsonPath("$.authorId", is(100)))
                .andExpect(jsonPath("$.authorName", is("just user")))
                .andExpect(jsonPath("$.authorImage", is("user.image.com")))
                .andExpect(jsonPath("$.description", is("description0")))
                .andExpect(jsonPath("$.viewCount", is(2)))
                .andExpect(jsonPath("$.authorReputation", is(5)))
                .andExpect(jsonPath("$.countAnswer", is(0)))
                .andExpect(jsonPath("$.countValuable", is(0)))
                .andExpect(jsonPath("$.persistDateTime", is("1990-10-10T00:00:00")))
                .andExpect(jsonPath("$.lastUpdateDateTime", is("1990-10-10T00:00:00")))
        ;

        //Не существующий ID вопроса
        this.mockMvc.perform(get(URL, incorrectId).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    public void should_return_status_not_found() throws Exception {
        username = "user@mail.ru";
        password = "user";

        mockMvc.perform(post("/api/user/question/99/upVote").header("Authorization", getToken(username, password)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    public void testing_upVote_and_downVote() throws Exception {
        username = "user@mail.ru";
        password = "user";
        token = getToken(username, password);

        Assert.assertEquals(0L, getReputationByIdUser(102L));

        // положительный голос 100-ого
        mockMvc.perform(post("/api/user/question/102/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        Assert.assertEquals(10L, getReputationByIdUser(102L));

        // повторный положительный голос 100-ого
        mockMvc.perform(post("/api/user/question/102/upVote").header("Authorization", token))
                .andExpect(status().isBadRequest());

        Assert.assertEquals(10L, getReputationByIdUser(102L));

        // положительный голос 100-ого за другой вопрос
        mockMvc.perform(post("/api/user/question/101/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        Assert.assertEquals(10L, getReputationByIdUser(102L));

        username = "user2@mail.ru";
        password = "user";
        token = getToken(username, password);

        // положительный голос 101-ого
        mockMvc.perform(post("/api/user/question/102/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(2)));

        Assert.assertEquals(20L, getReputationByIdUser(102L));

        // отрицательный голос 101-ого
        mockMvc.perform(post("/api/user/question/102/downVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));

        Assert.assertEquals(5L, getReputationByIdUser(102L));

        // повторный отрицательный голос 101-ого
        mockMvc.perform(post("/api/user/question/102/downVote").header("Authorization", token))
                .andExpect(status().isBadRequest());

        Assert.assertEquals(5L, getReputationByIdUser(102L));
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    public void user_votes_himself_method_should_return_status_bad_request() throws Exception {
        username = "user@mail.ru";
        password = "user";

        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", getToken(username, password)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    public void getPageByTagsIfNecessaryTest() throws Exception {
        //Некорретный запрос к серверу, отсутствует необходимый параметр "page"
        mockMvc.perform(get("/api/user/question?items=10")
                .param("trackedTag", "")
                .header("Authorization", getToken("user@mail.ru", "user")))
                .andExpect(status().isInternalServerError());

        //Запрос на получение всех вопросов юзера, который трекает один тэг и игнорирует другой
        mockMvc.perform(get("/api/user/question?page=1&items=10").header("Authorization", getToken("user@mail.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(1)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(4)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(10)))
                .andExpect(jsonPath("$.items.length()", equalTo(1)))
                .andExpect(jsonPath("$.items[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].title", equalTo("title0")))
                .andExpect(jsonPath("$.items[0].authorId", equalTo(100)))
                .andExpect(jsonPath("$.items[0].authorName", equalTo("just user")))
                .andExpect(jsonPath("$.items[0].authorImage", equalTo("user.image.com")))
                .andExpect(jsonPath("$.items[0].description", equalTo("description0")))
                .andExpect(jsonPath("$.items[0].viewCount", equalTo(0)))
                .andExpect(jsonPath("$.items[0].authorReputation", equalTo(5)))
                .andExpect(jsonPath("$.items[0].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[0].countValuable", equalTo(0)))
                .andExpect(jsonPath("$.items[0].persistDateTime", equalTo("1990-10-10T00:00:00")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description", equalTo("tag_1")));

        //Запрос на получение всех вопросов юзера, который трекает один тэг и не имеет игнорируемых
        mockMvc.perform(get("/api/user/question?page=1&items=10")
                .header("Authorization", getToken("user2@mail.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(1)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(4)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(10)))
                .andExpect(jsonPath("$.items.length()", equalTo(3)))

                .andExpect(jsonPath("$.items[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].title", equalTo("title0")))
                .andExpect(jsonPath("$.items[0].authorId", equalTo(100)))
                .andExpect(jsonPath("$.items[0].authorName", equalTo("just user")))
                .andExpect(jsonPath("$.items[0].authorImage", equalTo("user.image.com")))
                .andExpect(jsonPath("$.items[0].description", equalTo("description0")))
                .andExpect(jsonPath("$.items[0].viewCount", equalTo(0)))
                .andExpect(jsonPath("$.items[0].authorReputation", equalTo(5)))
                .andExpect(jsonPath("$.items[0].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[0].countValuable", equalTo(0)))
                .andExpect(jsonPath("$.items[0].persistDateTime", equalTo("1990-10-10T00:00:00")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description", equalTo("tag_1")))

                .andExpect(jsonPath("$.items[1].id", equalTo(101)))
                .andExpect(jsonPath("$.items[1].title", equalTo("title1")))
                .andExpect(jsonPath("$.items[1].authorId", equalTo(101)))
                .andExpect(jsonPath("$.items[1].authorName", equalTo("just user2")))
                .andExpect(jsonPath("$.items[1].authorImage", equalTo("user2.image.com")))
                .andExpect(jsonPath("$.items[1].description", equalTo("description1")))
                .andExpect(jsonPath("$.items[1].viewCount", equalTo(0)))
                .andExpect(jsonPath("$.items[1].authorReputation", equalTo(10)))
                .andExpect(jsonPath("$.items[1].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[1].countValuable", equalTo(0)))
                .andExpect(jsonPath("$.items[1].persistDateTime", equalTo("1990-10-10T00:00:00")))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[1].listTagDto[0].description", equalTo("tag_1")))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id", equalTo(101)))
                .andExpect(jsonPath("$.items[1].listTagDto[1].name", equalTo("tag_name_2")))
                .andExpect(jsonPath("$.items[1].listTagDto[1].description", equalTo("tag_2")))

                .andExpect(jsonPath("$.items[2].id", equalTo(103)))
                .andExpect(jsonPath("$.items[2].title", equalTo("title3")))
                .andExpect(jsonPath("$.items[2].authorId", equalTo(100)))
                .andExpect(jsonPath("$.items[2].authorName", equalTo("just user")))
                .andExpect(jsonPath("$.items[2].authorImage", equalTo("user.image.com")))
                .andExpect(jsonPath("$.items[2].description", equalTo("description3")))
                .andExpect(jsonPath("$.items[2].viewCount", equalTo(0)))
                .andExpect(jsonPath("$.items[2].authorReputation", equalTo(5)))
                .andExpect(jsonPath("$.items[2].countAnswer", equalTo(1)))
                .andExpect(jsonPath("$.items[2].countValuable", equalTo(0)))
                .andExpect(jsonPath("$.items[2].persistDateTime", equalTo("1990-10-10T00:00:00")))
                .andExpect(jsonPath("$.items[2].listTagDto[0].id", equalTo(101)))
                .andExpect(jsonPath("$.items[2].listTagDto[0].name", equalTo("tag_name_2")))
                .andExpect(jsonPath("$.items[2].listTagDto[0].description", equalTo("tag_2")));
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    void addNewQuestion_title_empty_or_null() throws Exception {

        username = "user@mail.ru";
        password = "user";

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        List<TagDto> tags = new ArrayList<>();
        TagDto tag = new TagDto();
        tag.setName("tag_name");
        tag.setDescription("tag_description");
        tags.add(tag);
        questionCreateDto.setDescription("question_description");
        questionCreateDto.setTags(tags);

        mockMvc.perform(post(resourceQuestionControllerUrl).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(questionCreateDto)).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    void addNewQuestion_description_empty_or_null() throws Exception {

        username = "user@mail.ru";
        password = "user";

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        List<TagDto> tags = new ArrayList<>();
        TagDto tag = new TagDto();
        tag.setName("tag_name");
        tag.setDescription("tag_description");
        tags.add(tag);
        questionCreateDto.setTitle("question_title");
        questionCreateDto.setTags(tags);

        mockMvc.perform(post(resourceQuestionControllerUrl).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(questionCreateDto)).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    void addNewQuestion_without_tags() throws Exception {

        username = "user@mail.ru";
        password = "user";

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("question_title");
        questionCreateDto.setDescription("question_description");

        mockMvc.perform(post(resourceQuestionControllerUrl).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(questionCreateDto)).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    void addNewQuestion_with_new_tags() throws Exception {

        username = "user@mail.ru";
        password = "user";

        //проверка, что такого тега нет в базе
        List<Tag> newTagListBeforePersist = (List<Tag>) entityManager.
                createQuery("select t from Tag t where t.name = :name and t.description = :description").
                setParameter("name", "new_tag_name").
                setParameter("description", "new_tag_description").getResultList();
        assertTrue(Collections.isEmpty(newTagListBeforePersist));

        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        List<TagDto> tags = new ArrayList<>();
        TagDto tag = new TagDto();
        tag.setName("new_tag_name");
        tag.setDescription("new_tag_description");
        tags.add(tag);
        questionCreateDto.setTitle("question_title");
        questionCreateDto.setDescription("question_description");
        questionCreateDto.setTags(tags);

        //отправляем запрос на персист вопроса с новым тегом
        mockMvc.perform(post(resourceQuestionControllerUrl).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(questionCreateDto)).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.title", is("question_title"))).
                andExpect(jsonPath("$.description", is("question_description"))).
                andExpect(jsonPath("$.listTagDto.*", hasSize(1))).
                andExpect(jsonPath("$.listTagDto[0].name", is("new_tag_name"))).
                andExpect(jsonPath("$.listTagDto[0].description", is("new_tag_description")));

        //проверяем наличие вопроса в бд, после сохранения
        List<Question> questionAfterPersist = (List<Question>) entityManager.
                createQuery("select q from Question q where q.title = :title and q.description = :description").
                setParameter("title", "question_title").
                setParameter("description", "question_description").getResultList();
        Long questionId = questionAfterPersist.get(0).getId();

        //проверяем наличие тега в бд после сохранения
        List<Tag> newTagListAfterPersist = (List<Tag>) entityManager.
                createQuery("select t from Tag t where t.name = :name and t.description = :description").
                setParameter("name", "new_tag_name").
                setParameter("description", "new_tag_description").getResultList();
        Long tagId = newTagListAfterPersist.get(0).getId();

        //проверяем привязку тега к вопросу после сохранеия
        assertFalse((entityManager.createNativeQuery("select * from question_has_tag where question_id = :questionId and tag_id = :tagId").
                setParameter("questionId", questionId).setParameter("tagId", tagId).getHints()).isEmpty());
        ;
    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    void addNewQuestion_with_existing_tags() throws Exception {

        username = "user@mail.ru";
        password = "user";

        List<Tag> existingTagList = (List<Tag>) entityManager.
                createQuery("select t from Tag t where t.name = :name and t.description = :description").
                setParameter("name", "tag_name_1").
                setParameter("description", "tag_1").getResultList();
        assertFalse(Collections.isEmpty(existingTagList));


        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        List<TagDto> tags = new ArrayList<>();
        TagDto tag = new TagDto();
        tag.setName("tag_name_1");
        tag.setDescription("tag_1");
        tags.add(tag);
        questionCreateDto.setTitle("question_title");
        questionCreateDto.setDescription("question_description");
        questionCreateDto.setTags(tags);

        mockMvc.perform(post(resourceQuestionControllerUrl).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(questionCreateDto)).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.title", is("question_title"))).
                andExpect(jsonPath("$.description", is("question_description"))).
                andExpect(jsonPath("$.listTagDto.*", hasSize(1))).
                andExpect(jsonPath("$.listTagDto[0].name", is("tag_name_1"))).
                andExpect(jsonPath("$.listTagDto[0].description", is("tag_1")));

        //проверяем наличие вопроса в бд, после сохранения
        List<Question> questionAfterPersist = (List<Question>) entityManager.
                createQuery("select q from Question q where q.title = :title and q.description = :description").
                setParameter("title", "question_title").
                setParameter("description", "question_description").getResultList();
        Long questionId = questionAfterPersist.get(0).getId();

        //проверяем наличие тега в бд после сохранения
        List<Tag> existingTagListAfterPersist = (List<Tag>) entityManager.
                createQuery("select t from Tag t where t.name = :name and t.description = :description").
                setParameter("name", "tag_name_1").
                setParameter("description", "tag_1").getResultList();
        Long tagId = existingTagListAfterPersist.get(0).getId();

        //проверяем привязку тега к вопросу после сохранеия
        assertFalse((entityManager.createNativeQuery("select * from question_has_tag where question_id = :questionId and tag_id = :tagId").
                setParameter("questionId", questionId).setParameter("tagId", tagId).getHints()).isEmpty());
        ;
    }

    //Получение вопросов без ответов с пагинацией
    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    public void getQuestionsWithoutAnswersPage() throws Exception {

        //Некорректный запрос к серверу, отсутствует необходимый параметр "page"
        mockMvc.perform(get("/api/user/question/noAnswer")
                        .header("Authorization", getToken("user@mail.ru", "user")))
                .andExpect(status().isInternalServerError());

        //Запрос на получение всех вопросов без ответов для юзера, который отслеживает один тэг и игнорирует другой
        mockMvc.perform(get("/api/user/question/noAnswer?page=1&items=10")
                        .header("Authorization", getToken("user@mail.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(1)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(4)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(10)))
                .andExpect(jsonPath("$.items.length()", equalTo(1)))

                .andExpect(jsonPath("$.items[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].title", equalTo("title0")))
                .andExpect(jsonPath("$.items[0].description", equalTo("description0")))
                .andExpect(jsonPath("$.items[0].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description", equalTo("tag_1")));

        //Запрос на получение всех вопросов без ответов для юзера, который отслеживает один тэг и не имеет игнорируемых
        mockMvc.perform(get("/api/user/question/noAnswer?page=1&items=10")
                        .header("Authorization", getToken("user2@mail.ru", "user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", equalTo(1)))
                .andExpect(jsonPath("$.totalPageCount", equalTo(1)))
                .andExpect(jsonPath("$.totalResultCount", equalTo(4)))
                .andExpect(jsonPath("$.itemsOnPage", equalTo(10)))
                .andExpect(jsonPath("$.items.length()", equalTo(2)))

                .andExpect(jsonPath("$.items[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].title", equalTo("title0")))
                .andExpect(jsonPath("$.items[0].description", equalTo("description0")))
                .andExpect(jsonPath("$.items[0].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[0].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[0].listTagDto[0].description", equalTo("tag_1")))

                .andExpect(jsonPath("$.items[1].id", equalTo(101)))
                .andExpect(jsonPath("$.items[1].title", equalTo("title1")))
                .andExpect(jsonPath("$.items[1].description", equalTo("description1")))
                .andExpect(jsonPath("$.items[1].countAnswer", equalTo(0)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].id", equalTo(100)))
                .andExpect(jsonPath("$.items[1].listTagDto[0].name", equalTo("tag_name_1")))
                .andExpect(jsonPath("$.items[1].listTagDto[0].description", equalTo("tag_1")))
                .andExpect(jsonPath("$.items[1].listTagDto[1].id", equalTo(101)))
                .andExpect(jsonPath("$.items[1].listTagDto[1].name", equalTo("tag_name_2")))
                .andExpect(jsonPath("$.items[1].listTagDto[1].description", equalTo("tag_2")));
    }

    //Получение комментариев у существующего вопроса
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml"
    }, cleanBefore = true, cleanAfter = true)
    @Test
    public void getAllCommentQuestion() throws Exception {
        mockMvc.perform(get("/api/user/question/101/comment")
                        .header("Authorization", getToken("user@mail.ru", "user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].questionId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imageLink").value("user.image.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reputation").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("Question comment good"))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].questionId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imageLink").value("user2.image.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reputation").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("Question comment bad"));
    }

    //Получение комментариев у несуществующего вопроса
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml"
    }, cleanBefore = true, cleanAfter = true)
    @Test
    public void getAllCommentQuestionNotFound() throws Exception {
        mockMvc.perform(get("/api/user/question/901/comment")
                        .header("Authorization", getToken("user@mail.ru", "user")))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //Получение комментариев у вопроса без комментариев
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/comments.yml",
            "resource_question_controller/comments_question.yml",
            "resource_question_controller/reputations.yml",
            "resource_question_controller/tags.yml",
            "resource_question_controller/tags_on_question.yml",
            "resource_question_controller/tags_tracked.yml",
            "resource_question_controller/tags_ignored.yml",
    }, cleanBefore = true, cleanAfter = true)
    @Test
    public void getEmptyListCommentQuestion() throws Exception {
        mockMvc.perform(get("/api/user/question/100/comment")
                        .header("Authorization", getToken("user@mail.ru", "user")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    //Получение количества вопросов
    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getCountQuestion() throws Exception {
        String responseMessage = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/question/count")
                        .header("Authorization", getToken("user@mail.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseMessage).isEqualTo(Long.toString(4));
    }

    //Получение вопросов если их нет
    @Test
    @DataSet(value = {"resource_question_controller/users.yml",
            "resource_question_controller/roles.yml",
            "resource_question_controller/no_questions/questions.yml"
    }, cleanBefore = true, cleanAfter = true)
    public void getCountQuestionZero() throws Exception {
        String responseMessage = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/question/count")
                        .header("Authorization", getToken("user@mail.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseMessage).isEqualTo(Long.toString(0));
    }
}
