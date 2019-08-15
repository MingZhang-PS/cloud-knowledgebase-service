package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.repository.GeneralSettingRepository;
import com.sap.fsm.knowledgebase.domain.model.GeneralSetting;
import com.sap.fsm.springboot.starter.test.annotation.Integration;
import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.SettingPresentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.hamcrest.collection.IsEmptyCollection;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

@Integration
class KnowledgeGeneralSettingApiTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private GeneralSettingRepository repository;

        // @MockBean
        // private GeneralSettingRepository mockRepository;

        private String someKey = "enabled";
        private String basePath = "/api/knowledge-base/v1/general-settings";
        private GeneralSetting generalSetting;
        private static final ObjectMapper om = new ObjectMapper();
        private static final ModelMapper mm = new ModelMapper();

        @BeforeEach
        void initTestCase() {
                generalSetting = new GeneralSetting();
                generalSetting.setKey(someKey);
                generalSetting.setLastChanged(new Date());
                generalSetting.setValue("false");
        }

        @Test
        public void findGeneralSettingByKeyOK() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(
                                get(basePath + "/" + someKey).accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isOk()).andExpect(jsonPath("$.key", is(generalSetting.getKey())))
                                .andExpect(jsonPath("$.value", is(generalSetting.getValue())));
        }

        @Test
        public void findGeneralSettingByKeyFails() throws Exception {
                // given

                // when
                ResultActions result = mockMvc
                                .perform(get(basePath + "/" + UUID.randomUUID().toString())
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isNotFound());
        }

        @Test
        public void findGeneralSettingsNotEmpty() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(
                                get(basePath + "?page=0&size=50").accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results").isArray())
                                .andExpect(jsonPath("$.results", hasSize(1)))
                                .andExpect(jsonPath("$.results[0].key", is(generalSetting.getKey())))
                                .andExpect(jsonPath("$.totalElements", is(1)));
        }

        @Test
        public void findGeneralSettingsEmpty() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(get(basePath + "?page=10000&size=50")
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.results", IsEmptyCollection.empty()));
        }

        @Test
        public void createGeneralSettingWithoutKey() throws Exception {
                // given
                generalSetting.setKey(null);

                // when
                ResultActions result = mockMvc.perform(post(basePath)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(generalSetting, GeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isBadRequest());
        }

        @Test
        public void createGeneralSettingPresent() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(post(basePath)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(generalSetting, GeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isConflict()).andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.title",
                                                is(new SettingPresentException(generalSetting.getKey()).getReason())));
        }

        @Test
        public void createGeneralSettingSuccessfully() throws Exception {
                // given
                GeneralSetting testSetting = new GeneralSetting();
                testSetting.setKey("somekey");
                testSetting.setValue("somevalue");
                // when
                ResultActions result = mockMvc.perform(post(basePath)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(testSetting, GeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.key", is(testSetting.getKey())))
                                .andExpect(jsonPath("$.value", is(testSetting.getValue())));

                repository.deleteByKey(testSetting.getKey());
        }

        @Test
        public void updateGeneralSettingNotFound() throws Exception {
                // given
                UUID randomID = UUID.randomUUID();
                generalSetting.setKey(randomID.toString());
                // when
                ResultActions result = mockMvc.perform(put(basePath + "/" + randomID)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(generalSetting, GeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.title", is(
                                                new ResourceNotExistException(generalSetting.getKey()).getReason())));
        }

        @Test
        public void updateGeneralSettingSuccessfully() throws Exception {
                // given
                GeneralSetting testSetting = new GeneralSetting();
                testSetting.setKey("somekey");
                testSetting.setValue("somevalue");
                repository.save(testSetting);
                // when
                GeneralSettingDto requestDto = mm.map(testSetting, GeneralSettingDto.class);
                requestDto.setValue("change value");
                ResultActions result = mockMvc.perform(put(basePath + "/" + testSetting.getKey())
                                .contentType(APPLICATION_JSON_UTF8).content(om.writeValueAsString(requestDto))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.value", is("change value")))
                                .andExpect(jsonPath("$.key", is(testSetting.getKey())));
                repository.deleteByKey(testSetting.getKey());
        }
}
