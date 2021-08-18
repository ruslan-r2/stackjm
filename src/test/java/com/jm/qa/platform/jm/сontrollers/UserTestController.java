package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserTestController extends AbstractIntegrationTest {

    private String URL = "/rest/api/users";

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldGetUserById() throws Exception {
        System.out.println();
    }

    @Test
    @DataSet(value = {"roles.yml","users.yml"},cleanAfter = true,cleanBefore = true)
    void testHello() throws Exception{
        this.mockMvc.perform(get(URL+"/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")));
    }

}
