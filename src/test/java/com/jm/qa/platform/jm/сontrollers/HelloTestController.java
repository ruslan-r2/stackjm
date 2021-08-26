package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloTestController extends AbstractIntegrationTest {

    private String URL = "/rest/api/test";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"roles.yml","users.yml"},cleanAfter = true,cleanBefore = true)
    void testAnonymousHello() throws Exception{
                this.mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
