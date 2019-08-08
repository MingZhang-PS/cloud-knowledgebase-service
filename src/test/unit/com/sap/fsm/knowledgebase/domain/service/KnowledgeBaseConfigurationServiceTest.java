package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypeNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypeCodePresentException;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.modelmapper.ModelMapper;
import org.json.JSONException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Unit
public class KnowledgeBaseConfigurationServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KnowledgeBaseProviderTypeRepository knowledgeBaseProviderTypeRepository;

    @InjectMocks
    private KnowledgeBaseConfigurationService knowledgeBaseConfigurationService;

    private static KnowledgeBaseProviderType fakeType;
    private static KnowledgeBaseProviderTypeDto requestDto;
    private static List<KnowledgeBaseProviderType> fakeTypeList;
    private static UUID someId = UUID.fromString("6f6c1b6e-0520-4a27-b37f-f34be2d964bf");

    @BeforeAll
    public static void beforeAll() {
        fakeType = new KnowledgeBaseProviderType();
        fakeType.setCode("MindTouch");
        fakeType.setName("test");
        fakeType.setId(someId);
        fakeType.setLastChanged(new Date());

        fakeTypeList = new ArrayList<KnowledgeBaseProviderType>();
        fakeTypeList.add(fakeType);
    }

    @BeforeEach
    public void initMocks() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requestDto =  new KnowledgeBaseProviderTypeDto();
        requestDto.setCode(fakeType.getCode());
        requestDto.setName(fakeType.getName());
        requestDto.setId(fakeType.getId());
        requestDto.setLastChanged(fakeType.getLastChanged());
    }

    @DisplayName("Test knowledgeBaseConfigurationService get provider type by id successfully")
    @Test
    void shouldGetProviderTypeByIdSuccessfully() {
        // given
        given(knowledgeBaseProviderTypeRepository.findById(someId)).willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        KnowledgeBaseProviderTypeDto findResult = knowledgeBaseConfigurationService
                .findByKnowledgeBaseProviderTypeId(someId);

        // then
        assertEquals(fakeType.getCode(), findResult.getCode());
        assertEquals(fakeType.getId(), findResult.getId());
        assertEquals(fakeType.getName(), findResult.getName());
    }

    @DisplayName("Test knowledgeBaseConfigurationService get provider type by id fails due to resource not found")
    @Test
    void shouldGetProviderTypeByIdNotFound() {
        given(knowledgeBaseProviderTypeRepository.findById(someId)).willReturn(Optional.empty());
        Assertions.assertThrows(ProviderTypeNotExistException.class, () -> {
            knowledgeBaseConfigurationService.findByKnowledgeBaseProviderTypeId(someId);
        });
    }

    @DisplayName("Test knowledgeBaseConfigurationService update provider type successfully")
    @Test
    void shouldUpdateProviderTypeSuccessfully() {
        // given  
        requestDto.setName("Hello");
        given(knowledgeBaseProviderTypeRepository.findByIdAndCode(someId, requestDto.getCode()))
                .willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        KnowledgeBaseProviderTypeDto updateResult = knowledgeBaseConfigurationService
                .updateByKnowledgeBaseProviderTypeId(someId, requestDto);

        // then
        assertEquals(requestDto.getName(), updateResult.getName());
        assertEquals(fakeType.getCode(), updateResult.getCode());
        assertEquals(fakeType.getId(), updateResult.getId());
    }

    @DisplayName("Test knowledgeBaseConfigurationService update provider type fails due to resource not found")
    @Test
    void shouldUpdateProviderTypeFailsNotFound() {
        given(knowledgeBaseProviderTypeRepository.findByIdAndCode(someId, requestDto.getCode())).willReturn(Optional.empty());
        Assertions.assertThrows(ProviderTypeNotExistException.class, () -> {
            knowledgeBaseConfigurationService.updateByKnowledgeBaseProviderTypeId(someId, requestDto);
        });
    }

    @DisplayName("Test knowledgeBaseConfigurationService create provider type successfully")
    @Test
    void shouldCreateProviderTypeSuccessfully() {
        // given
        given(knowledgeBaseProviderTypeRepository.findByCode(requestDto.getCode()))
                .willReturn(new ArrayList<KnowledgeBaseProviderType>());
        given(knowledgeBaseProviderTypeRepository.save(any()))
                .willReturn(fakeType);
        given(modelMapper.map(any(), eq(KnowledgeBaseProviderType.class))).willReturn(fakeType);
        given(modelMapper.map(any(), eq(KnowledgeBaseProviderTypeDto.class))).willReturn(requestDto);
        // when
        KnowledgeBaseProviderTypeDto saveResult = knowledgeBaseConfigurationService
                .createKnowledgeBaseProviderType(requestDto);

        // then
        assertEquals(fakeType.getName(), saveResult.getName());
        assertEquals(fakeType.getCode(), saveResult.getCode());
        assertEquals(fakeType.getId(), saveResult.getId());
    }

    @DisplayName("Test knowledgeBaseConfigurationService create provider type fails due to code duplication")
    @Test
    void shouldCreateProviderTypeFailsConflict() {
        given(knowledgeBaseProviderTypeRepository.findByCode(requestDto.getCode())).willReturn(fakeTypeList);
        Assertions.assertThrows(ProviderTypeCodePresentException.class, () -> {
            knowledgeBaseConfigurationService.createKnowledgeBaseProviderType(requestDto);
        });
    }

    @DisplayName("Test knowledgeBaseConfigurationService return provider type list as empty list successfully")
    @Test
    void shouldGetProviderTypesSuccessfully() {
        // given
        given(knowledgeBaseProviderTypeRepository.findAll(PageRequest.of(0,1)))
                .willReturn(new PageImpl<KnowledgeBaseProviderType>(new ArrayList<KnowledgeBaseProviderType>()));
        PaginationRecord<KnowledgeBaseProviderTypeDto> mappedProviders = new PaginationRecord<KnowledgeBaseProviderTypeDto>();
        mappedProviders.setContent(new ArrayList<KnowledgeBaseProviderTypeDto>());
        given(modelMapper.map(any(), any())).willReturn(mappedProviders);
        // when
        PaginationRecord<KnowledgeBaseProviderTypeDto> findResults = knowledgeBaseConfigurationService
                .findKnowledgeBaseProviderTypes(PageRequest.of(0,1));

        // then
        assertEquals(0, findResults.getContent().size());
        verify(modelMapper, times(1)).map(any(), eq(new PaginationRecord<KnowledgeBaseProviderTypeDto>().getClass()));
    }
}