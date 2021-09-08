package com.jm.qa.platform.jm.сontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class TestTopTagController {
    private String URL = "/api/user/tag/related";

    @Autowired
    private MockMvc mockMvc;
}
