package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;
import com.sap.fsm.springboot.starter.test.annotation.Integration;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypePresentException;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
class KnowledgeBaseProviderTypeApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KnowledgeBaseProviderTypeRepository mockRepository;

    // @Value("${application.name}")
    // private String applicationName;

    private UUID someId = UUID.fromString("9049529f-f6f2-4881-87b3-bcbc4ab3f886");
    private String basePath = "/api/knowledge-base/v1/";
    private KnowledgeBaseProviderType providerType;
    private static final ObjectMapper om = new ObjectMapper();
    private static final ModelMapper mm = new ModelMapper();

    @BeforeEach
    void initTestCase() {
        providerType = new KnowledgeBaseProviderType();
        providerType.setId(someId);
        providerType.setCode("SomeFamousKB");
        providerType.setLastChanged(new Date());
        providerType.setName("for test");
    }

    @Test
    public void findProviderTypeByIdOK() throws Exception {
        // given
        given(mockRepository.findById(someId)).willReturn(Optional.of(providerType));

        // when
        ResultActions result = mockMvc
                .perform(get(basePath + "provider-types" + "/" + someId.toString()).accept(APPLICATION_JSON_UTF8));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(someId.toString())))
                .andExpect(jsonPath("$.code", is(providerType.getCode())))
                .andExpect(jsonPath("$.name", is(providerType.getName())));
        verify(mockRepository, times(1)).findById(someId);
    }

    @Test
    public void findProviderTypeFails() throws Exception {
        // given
        given(mockRepository.findById(someId)).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc
                .perform(get(basePath + "provider-types" + "/" + someId.toString()).accept(APPLICATION_JSON_UTF8));

        // then
        result.andExpect(status().isNotFound());
        verify(mockRepository, times(1)).findById(someId);
    }

    @Test
    public void findProviderTypesEmpty() throws Exception {
        // given
        given(mockRepository.findAll(PageRequest.of(0, 1)))
                .willReturn(new PageImpl<KnowledgeBaseProviderType>(new ArrayList<KnowledgeBaseProviderType>()));

        // when
        ResultActions result = mockMvc
                .perform(get(basePath + "provider-types" + "?page=0&size=1").accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results", IsEmptyCollection.empty()))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(mockRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void findProviderTypesNotEmpty() throws Exception {
        // given
        List<KnowledgeBaseProviderType> list = new ArrayList<KnowledgeBaseProviderType>();
        list.add(providerType);
        given(mockRepository.findAll(PageRequest.of(0, 1))).willReturn(new PageImpl<KnowledgeBaseProviderType>(list));

        // when
        ResultActions result = mockMvc
                .perform(get(basePath + "provider-types" + "?page=0&size=1").accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results", hasSize(1))).andExpect(jsonPath("$.totalElements", is(1)));

        verify(mockRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void createProviderTypeWithoutCode() throws Exception {
        // given
        providerType.setCode(null);

        // when
        ResultActions result = mockMvc.perform(post(basePath + "provider-types").contentType(APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(mm.map(providerType, KnowledgeBaseProviderTypeDto.class))).accept(APPLICATION_JSON_UTF8));

        // then
        result.andExpect(status().isBadRequest());
        verify(mockRepository, times(0)).save(any());
    }

    @Test
    public void createProviderTypePresent() throws Exception {
        // given

        given(mockRepository.findByCode(providerType.getCode())).willReturn(Optional.of(providerType));

        // when
        ResultActions result = mockMvc
                .perform(post(basePath + "provider-types" ).contentType(APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(mm.map(providerType, KnowledgeBaseProviderTypeDto.class))).accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isConflict())
        .andExpect(jsonPath("$.status", is(409)))
        .andExpect(jsonPath("$.title", is(new ProviderTypePresentException(providerType.getCode()).getReason())));

        verify(mockRepository, times(0)).save(any());
    }
  
    @Test
    public void createProviderTypeSuccessfully() throws Exception {
        // given
        given(mockRepository.findByCode(providerType.getCode())).willReturn(Optional.empty());
        given(mockRepository.save(any(KnowledgeBaseProviderType.class))).willReturn(providerType);

        // when
        ResultActions result = mockMvc
                .perform(post(basePath + "provider-types" ).contentType(APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(mm.map(providerType, KnowledgeBaseProviderTypeDto.class))).accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(providerType.getCode())))
        .andExpect(jsonPath("$.name", is(providerType.getName())));

        verify(mockRepository, times(1)).save(providerType);
    }
    
    @Test
    public void updateProviderTypeNotFound() throws Exception {
        // given
        given(mockRepository.findByIdAndCode(any(), any())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc
                .perform(put(basePath + "provider-types" + "/" + providerType.getId() ).contentType(APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(mm.map(providerType, KnowledgeBaseProviderTypeDto.class))).accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.title", is(new ResourceNotExistException(providerType.getId()).getReason())));

        verify(mockRepository, times(0)).save(any());
    }

    @Test
    public void updateProviderTypeSuccessfully() throws Exception {
        // given
        given(mockRepository.findByIdAndCode(any(), any())).willReturn(Optional.of(providerType));

        // when
        KnowledgeBaseProviderTypeDto requestDto =  mm.map(providerType, KnowledgeBaseProviderTypeDto.class);
        requestDto.setName("change name");
        ResultActions result = mockMvc
                .perform(put(basePath + "provider-types" + "/" + providerType.getId() ).contentType(APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(requestDto)).accept(APPLICATION_JSON_UTF8));

        // then
        result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("change name")))
        .andExpect(jsonPath("$.code", is(providerType.getCode())));
    }

}
