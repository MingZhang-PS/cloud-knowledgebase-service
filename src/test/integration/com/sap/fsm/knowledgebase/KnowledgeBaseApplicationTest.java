package com.sap.fsm.knowledgebase;

import com.sap.fsm.springboot.starter.test.annotation.Integration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Integration
class KnowledgeBaseApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${application.name}")
    private String applicationName;

    @Test
    void shouldReturnNewTemplate() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get("/" + applicationName + "/actuator/info").accept(
                APPLICATION_JSON_UTF8));

        // then
        result.andExpect(status().isOk());
    }

}

