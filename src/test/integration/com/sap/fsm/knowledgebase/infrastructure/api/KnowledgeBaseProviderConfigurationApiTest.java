package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.repository.ProviderConfigurationRepository;
import com.sap.fsm.knowledgebase.domain.model.ProviderConfiguration;
import com.sap.fsm.springboot.starter.test.annotation.Integration;
import com.sap.fsm.knowledgebase.domain.dto.ProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.exception.OtherActiveConfigurationPresentException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderConfigurationPresentException;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

@Integration
class KnowledgeBaseProviderConfigurationApiTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProviderConfigurationRepository providerConfigurationRepository;

        // @Autowired
        // private ProviderTypeRepository providerTypeRepository;

        private UUID someId = UUID.fromString("b3a69932-2c77-4354-a268-b2cc2b1f0061");
        private UUID providerTypeId = UUID.fromString("68d0e024-2bef-43c1-ad3c-00af7e48e9c4");
        private String providerTypeCode = "MindTouch";
        private String basePath = "/api/knowledge-base/v1/";
        private ProviderConfiguration providerConfiguration;
        private static final ObjectMapper om = new ObjectMapper();
        private static final ModelMapper mm = new ModelMapper();

        @BeforeEach
        void initTestCase() {
                providerConfiguration = new ProviderConfiguration();
                providerConfiguration.setId(someId);
                providerConfiguration.setIsActive(true);
                providerConfiguration.setProviderType(providerTypeId);
                providerConfiguration.setAdapterAuthType("NoAuthentication");
                providerConfiguration.setAdapterURL("https://www.baidu.com");
        }

        @AfterEach
        void cleanupTestCase() {
                providerConfigurationRepository.deleteAll();
        }

        @Test
        public void findProviderConfigurationByIdOK() throws Exception {
                // given
                providerConfigurationRepository.save(providerConfiguration);

                // when
                ResultActions result = mockMvc
                                .perform(get(basePath + "provider-configurations" + "/" + providerTypeCode)
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.providerType",
                                                is(providerConfiguration.getProviderType().toString())))
                                .andExpect(jsonPath("$.adapterAuthType",
                                                is(providerConfiguration.getAdapterAuthType())));
        }

        @Test
        public void findProviderConfigurationFails() throws Exception {
                // given

                // when
                ResultActions result = mockMvc
                                .perform(get(basePath + "provider-configurations" + "/" + UUID.randomUUID().toString())
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isNotFound());
        }

        @Test
        public void findConfigurationsEmpty() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(get(basePath + "provider-configurations" + "?page=0&size=50")
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.results", IsEmptyCollection.empty()));
        }

        @Test
        public void findConfigurationsNotEmpty() throws Exception {
                // given
                providerConfigurationRepository.save(providerConfiguration);
                // when
                ResultActions result = mockMvc.perform(get(basePath + "provider-configurations" + "?page=0&size=50")
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results").isArray())
                                .andExpect(jsonPath("$.results", hasSize(1)))
                                .andExpect(jsonPath("$.totalElements", is(1)));
        }

        @Test
        public void createConfigurationsFailsWithoutProviderType() throws Exception {
                // given
                providerConfiguration.setProviderType(null);

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-configurations")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(providerConfiguration, ProviderConfigurationDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isBadRequest());
        }

        @Test
        public void createConfigurationFailsInvalidProviderType() throws Exception {
                // given
                providerConfiguration.setProviderType(UUID.randomUUID());

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-configurations")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(providerConfiguration, ProviderConfigurationDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isNotFound());
        }

        @Test
        public void createConfigurationFailsAnotherConfigurationWithSameCodePresent() throws Exception {
                // given
                providerConfigurationRepository.save(providerConfiguration);
                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-configurations")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(providerConfiguration, ProviderConfigurationDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isConflict()).andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.title",
                                                is(new ProviderConfigurationPresentException(
                                                                providerConfiguration.getProviderType().toString())
                                                                                .getReason())));
        }

        @Test
        public void createConfigurationFailsAnotherConfigurationActive() throws Exception {
                // given
                providerConfigurationRepository.save(providerConfiguration);
                providerConfiguration.setProviderType(UUID.fromString("2234e454-2f12-40d8-a58a-3a5d06ccd8a6"));
                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-configurations")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(providerConfiguration, ProviderConfigurationDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isConflict()).andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.title",
                                                is(new OtherActiveConfigurationPresentException().getReason())));
        }

        @Test
        public void createConfigurationSuccessfully() throws Exception {
                // given

                // when
                ResultActions result = mockMvc.perform(post(basePath + "provider-configurations")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(providerConfiguration, ProviderConfigurationDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.providerType",
                                                is(providerConfiguration.getProviderType().toString())))
                                .andExpect(jsonPath("$.adapterAuthType",
                                                is(providerConfiguration.getAdapterAuthType())));
        }

        @Test
        public void updateConfigurationFailsProviderTypeNotFound() throws Exception {
                // given
                providerConfiguration.setProviderType(UUID.randomUUID());

                // when
                ResultActions result = mockMvc
                                .perform(put(basePath + "provider-configurations" + "/" + providerConfiguration.getId())
                                                .contentType(APPLICATION_JSON_UTF8)
                                                .content(om.writeValueAsString(mm.map(providerConfiguration,
                                                                ProviderConfigurationDto.class)))
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.title",
                                                is(new ResourceNotExistException(
                                                                providerConfiguration.getProviderType().toString())
                                                                                .getReason())));
        }

        @Test
        public void updateConfigurationFailsConfigurationNotExist() throws Exception {
                // given

                // when
                ResultActions result = mockMvc
                                .perform(put(basePath + "provider-configurations" + "/" + providerConfiguration.getId())
                                                .contentType(APPLICATION_JSON_UTF8)
                                                .content(om.writeValueAsString(mm.map(providerConfiguration,
                                                                ProviderConfigurationDto.class)))
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.title",
                                                is(new ResourceNotExistException(
                                                                providerConfiguration.getId().toString())
                                                                                .getReason())));
        }

        @Test
        public void updateConfigurationSuccessfully() throws Exception {
                // given
                providerConfigurationRepository.save(providerConfiguration);
                providerConfiguration.setAdapterAuthType("Basic");

                // when
                ResultActions result = mockMvc
                                .perform(put(basePath + "provider-configurations" + "/" + providerConfiguration.getId())
                                                .contentType(APPLICATION_JSON_UTF8)
                                                .content(om.writeValueAsString(mm.map(providerConfiguration,
                                                                ProviderConfigurationDto.class)))
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.providerType",
                                                is(providerConfiguration.getProviderType().toString())))
                                .andExpect(jsonPath("$.adapterAuthType",
                                                is(providerConfiguration.getAdapterAuthType())));
        }

}
