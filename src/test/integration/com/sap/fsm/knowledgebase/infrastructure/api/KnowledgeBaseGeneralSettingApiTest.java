package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseGeneralSettingRepository;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;
import com.sap.fsm.springboot.starter.test.annotation.Integration;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseGeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.SettingPresentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

@Integration
class KnowledgeGeneralSettingApiTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private KnowledgeBaseGeneralSettingRepository mockRepository;

        private UUID someId = UUID.fromString("9049529f-f6f2-4881-87b3-bcbc4ab3f886");
        private String basePath = "/api/knowledge-base/v1/";
        private KnowledgeBaseGeneralSetting generalSetting;
        private static final ObjectMapper om = new ObjectMapper();
        private static final ModelMapper mm = new ModelMapper();

        @BeforeEach
        void initTestCase() {
                generalSetting = new KnowledgeBaseGeneralSetting();
                generalSetting.setId(someId);
                generalSetting.setKey("SomeKey");
                generalSetting.setLastChanged(new Date());
                generalSetting.setValue("for test");
        }

        @Test
        public void findGeneralSettingByIdOK() throws Exception {
                // given
                given(mockRepository.findById(someId)).willReturn(Optional.of(generalSetting));

                // when
                ResultActions result = mockMvc.perform(get(basePath + "general-settings" + "/" + someId.toString())
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(someId.toString())))
                                .andExpect(jsonPath("$.key", is(generalSetting.getKey())))
                                .andExpect(jsonPath("$.value", is(generalSetting.getValue())));
                verify(mockRepository, times(1)).findById(someId);
        }

        @Test
        public void findGeneralSettingByIdFails() throws Exception {
                // given
                given(mockRepository.findById(someId)).willReturn(Optional.empty());

                // when
                ResultActions result = mockMvc.perform(get(basePath + "general-settings" + "/" + someId.toString())
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isNotFound());
                verify(mockRepository, times(1)).findById(someId);
        }

        @Test
        public void findGeneralSettingsEmpty() throws Exception {
                // given
                given(mockRepository.findAll(PageRequest.of(0, 1)))
                                .willReturn(new PageImpl<KnowledgeBaseGeneralSetting>(
                                                new ArrayList<KnowledgeBaseGeneralSetting>()));

                // when
                ResultActions result = mockMvc.perform(
                                get(basePath + "general-settings" + "?page=0&size=1").accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.results", IsEmptyCollection.empty()))
                                .andExpect(jsonPath("$.totalElements", is(0)));

                verify(mockRepository, times(1)).findAll(any(Pageable.class));
        }

        @Test
        public void findGeneralSettingsNotEmpty() throws Exception {
                // given
                List<KnowledgeBaseGeneralSetting> list = new ArrayList<KnowledgeBaseGeneralSetting>();
                list.add(generalSetting);
                given(mockRepository.findByKey(any(), eq(PageRequest.of(0, 1))))
                                .willReturn(new PageImpl<KnowledgeBaseGeneralSetting>(list));

                // when
                ResultActions result = mockMvc.perform(get(
                                basePath + "general-settings" + "?page=0&size=1" + "&key=" + generalSetting.getKey())
                                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results").isArray())
                                .andExpect(jsonPath("$.results", hasSize(1)))
                                .andExpect(jsonPath("$.results[0].key", is(generalSetting.getKey())))
                                .andExpect(jsonPath("$.totalElements", is(1)));

                verify(mockRepository, times(1)).findByKey(eq(generalSetting.getKey()), any(Pageable.class));
        }

        @Test
        public void createGeneralSettingWithoutKey() throws Exception {
                // given
                generalSetting.setKey(null);

                // when
                ResultActions result = mockMvc.perform(post(basePath + "general-settings")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(generalSetting, KnowledgeBaseGeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andExpect(status().isBadRequest());
                verify(mockRepository, times(0)).save(any());
        }

        @Test
        public void createGeneralSettingPresent() throws Exception {
                // given

                given(mockRepository.findByKey(generalSetting.getKey())).willReturn(Optional.of(generalSetting));

                // when
                ResultActions result = mockMvc.perform(post(basePath + "general-settings")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(generalSetting, KnowledgeBaseGeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isConflict()).andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.title",
                                                is(new SettingPresentException(generalSetting.getKey()).getReason())));

                verify(mockRepository, times(0)).save(any());
        }

        @Test
        public void createGeneralSettingSuccessfully() throws Exception {
                // given
                given(mockRepository.findByKey(generalSetting.getKey())).willReturn(Optional.empty());
                given(mockRepository.save(any(KnowledgeBaseGeneralSetting.class))).willReturn(generalSetting);

                // when
                ResultActions result = mockMvc.perform(post(basePath + "general-settings")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(generalSetting, KnowledgeBaseGeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.key", is(generalSetting.getKey())))
                                .andExpect(jsonPath("$.value", is(generalSetting.getValue())));

                verify(mockRepository, times(1)).save(generalSetting);
        }

        @Test
        public void updateGeneralSettingNotFound() throws Exception {
                // given
                given(mockRepository.findByIdAndKey(any(), any())).willReturn(Optional.empty());

                // when
                ResultActions result = mockMvc.perform(put(basePath + "general-settings" + "/" + generalSetting.getId())
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(om.writeValueAsString(
                                                mm.map(generalSetting, KnowledgeBaseGeneralSettingDto.class)))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.title",
                                                is(new ResourceNotExistException(generalSetting.getId()).getReason())));

        }

        @Test
        public void updateGeneralSettingSuccessfully() throws Exception {
                // given
                given(mockRepository.findByIdAndKey(any(), any())).willReturn(Optional.of(generalSetting));

                // when
                KnowledgeBaseGeneralSettingDto requestDto = mm.map(generalSetting,
                                KnowledgeBaseGeneralSettingDto.class);
                requestDto.setValue("change value");
                ResultActions result = mockMvc.perform(put(basePath + "general-settings" + "/" + generalSetting.getId())
                                .contentType(APPLICATION_JSON_UTF8).content(om.writeValueAsString(requestDto))
                                .accept(APPLICATION_JSON_UTF8));

                // then
                result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.value", is("change value")))
                                .andExpect(jsonPath("$.key", is(generalSetting.getKey())));
        }
}