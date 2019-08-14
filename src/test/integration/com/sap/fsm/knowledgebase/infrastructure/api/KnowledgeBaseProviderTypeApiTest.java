package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.springboot.starter.test.annotation.Integration;
import com.sap.fsm.knowledgebase.domain.dto.ProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypePresentException;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;

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

import java.util.UUID;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

@Integration
class KnowledgeBaseProviderTypeApiTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProviderTypeRepository repository;

        private UUID someId = UUID.fromString("68d0e024-2bef-43c1-ad3c-00af7e48e9c4");
        private String basePath = "/api/knowledge-base/v1/";
        private ProviderType providerType;
        private static final ObjectMapper om = new ObjectMapper();
        private static final ModelMapper mm = new ModelMapper();

        @BeforeEach
        void initTestCase() {
                providerType = new ProviderType();
                providerType.setId(someId);
                providerType.setCode("MindTouch");
                providerType.setLastChanged(new Date());
                providerType.setName("");
        }

        @Test
        public void findProviderTypeByIdOK() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(get(basePath + "provider-types" + "/" + someId.toString())
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(someId.toString())))
                                .andExpect(jsonPath("$.code", is(providerType.getCode())))
                                .andExpect(jsonPath("$.name", is(providerType.getName())));
        }

        @Test
        public void findProviderTypeFails() throws Exception {
                // given

                // when
                ResultActions result = mockMvc
                                .perform(get(basePath + "provider-types" + "/" + UUID.randomUUID().toString())
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isNotFound());
        }

        @Test
        public void findProviderTypesEmpty() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(
                                get(basePath + "provider-types" + "?page=1&size=50").accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.results", IsEmptyCollection.empty()));
        }

        @Test
        public void findProviderTypesNotEmpty() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(
                                get(basePath + "provider-types" + "?page=0&size=50").accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results").isArray())
                                .andExpect(jsonPath("$.results", hasSize(2)))
                                .andExpect(jsonPath("$.totalElements", is(2)));
        }

        @Test
        public void createProviderTypeWithoutCode() throws Exception {
                // given
                providerType.setCode(null);

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-types")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(providerType, ProviderTypeDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isBadRequest());
        }

        @Test
        public void createProviderTypePresent() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-types")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(providerType, ProviderTypeDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isConflict()).andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.title", is(
                                                new ProviderTypePresentException(providerType.getCode()).getReason())));
        }

        @Test
        public void createProviderTypeSuccessfully() throws Exception {
                // given
                ProviderType testProviderType = new ProviderType();
                testProviderType.setCode("somecode");
                testProviderType.setName("somename");

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-types")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(testProviderType, ProviderTypeDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.code", is(testProviderType.getCode())))
                                .andExpect(jsonPath("$.name", is(testProviderType.getName())));

                repository.deleteByCode(testProviderType.getCode());
        }

        @Test
        public void updateProviderTypeNotFound() throws Exception {
                // given
                UUID randomID = UUID.randomUUID();
                providerType.setId(randomID);

                // when
                ResultActions result = mockMvc.perform(put(basePath + "provider-types" + "/" + providerType.getId())
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(mm.map(providerType, ProviderTypeDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.title",
                                                is(new ResourceNotExistException(providerType.getId().toString())
                                                                .getReason())));
        }

        @Test
        public void updateProviderTypeSuccessfully() throws Exception {
                // given
                ProviderType testProviderType = new ProviderType();
                testProviderType.setId(UUID.randomUUID());
                testProviderType.setCode("somecode");
                testProviderType.setName("somename");
                repository.save(testProviderType);

                testProviderType.setName("change name");

                // when
                ProviderTypeDto requestDto = mm.map(testProviderType, ProviderTypeDto.class);
                requestDto.setName("change name");
                ResultActions result = mockMvc.perform(put(basePath + "provider-types" + "/" + testProviderType.getId())
                                .contentType(APPLICATION_JSON_UTF8).content(om.writeValueAsString(requestDto))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.name", is(testProviderType.getName())))
                                .andExpect(jsonPath("$.code", is(testProviderType.getCode())));
                repository.deleteByCode(testProviderType.getCode());
        }

}
