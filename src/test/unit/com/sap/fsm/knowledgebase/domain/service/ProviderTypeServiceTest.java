package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.ProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypePresentException;
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
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Unit
public class ProviderTypeServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProviderTypeRepository mockRepository;

    @InjectMocks
    private ProviderTypeService providerTypeService;

    private static ProviderType fakeType;
    private static ProviderTypeDto requestDto;
    private static List<ProviderType> fakeTypeList;
    private static String someCode = "SAP-MINDTOUCH";

    @BeforeAll
    public static void beforeAll() {
        fakeType = new ProviderType();
        fakeType.setCode(someCode);
        fakeType.setName("test");
        fakeType.setLastChanged(new Date());

        fakeTypeList = new ArrayList<ProviderType>();
        fakeTypeList.add(fakeType);
    }

    @BeforeEach
    public void initMocks() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requestDto = new ProviderTypeDto();
        requestDto.setCode(fakeType.getCode());
        requestDto.setName(fakeType.getName());
        requestDto.setLastChanged(fakeType.getLastChanged());
    }

    @DisplayName("Test ProviderTypeService get provider type by code successfully")
    @Test
    void shouldGetProviderTypeByCodeSuccessfully() {
        // given
        given(mockRepository.findByCode(someCode)).willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        ProviderTypeDto findResult = providerTypeService.findByProviderTypeCode(someCode);

        // then
        assertEquals(fakeType.getCode(), findResult.getCode());
        assertEquals(fakeType.getName(), findResult.getName());
    }

    @DisplayName("Test ProviderTypeService get provider type by code fails due to resource not found")
    @Test
    void shouldGetProviderTypeByCodeNotFound() {
        given(mockRepository.findByCode(someCode)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerTypeService.findByProviderTypeCode(someCode);
        });
    }

    @DisplayName("Test ProviderTypeService update provider type successfully")
    @Test
    void shouldUpdateProviderTypeSuccessfully() {
        // given
        requestDto.setName("Hello");
        given(mockRepository.findByCode(someCode)).willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        ProviderTypeDto updateResult = providerTypeService
                .updateByProviderTypeCode(someCode, requestDto);

        // then
        assertEquals(requestDto.getName(), updateResult.getName());
        assertEquals(fakeType.getCode(), updateResult.getCode());
    }

    @DisplayName("Test ProviderTypeService update provider type fails due to resource not found")
    @Test
    void shouldUpdateProviderTypeFailsNotFound() {
        given(mockRepository.findByCode(someCode)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerTypeService.updateByProviderTypeCode(someCode, requestDto);
        });
    }

    @DisplayName("Test ProviderTypeService create provider type successfully")
    @Test
    void shouldCreateProviderTypeSuccessfully() {
        // given
        given(mockRepository.findByCode(requestDto.getCode())).willReturn(Optional.empty());
        given(mockRepository.save(any())).willReturn(fakeType);
        given(modelMapper.map(any(), eq(ProviderType.class))).willReturn(fakeType);
        given(modelMapper.map(any(), eq(ProviderTypeDto.class))).willReturn(requestDto);
        // when
        ProviderTypeDto saveResult = providerTypeService
                .createProviderType(requestDto);

        // then
        assertEquals(fakeType.getName(), saveResult.getName());
        assertEquals(fakeType.getCode(), saveResult.getCode());
    }

    @DisplayName("Test ProviderTypeService create provider type fails due to code duplication")
    @Test
    void shouldCreateProviderTypeFailsConflict() {
        given(mockRepository.findByCode(requestDto.getCode())).willReturn(Optional.of(fakeType));
        Assertions.assertThrows(ProviderTypePresentException.class, () -> {
            providerTypeService.createProviderType(requestDto);
        });
    }

    @DisplayName("Test ProviderTypeService return provider type list as empty list successfully")
    @Test
    void shouldGetProviderTypesAsEmptyListSuccessfully() {
        // given
        given(mockRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<ProviderType>(new ArrayList<ProviderType>()));
        PaginationRecord<ProviderTypeDto> mappedProviders = new PaginationRecord<ProviderTypeDto>();
        mappedProviders.setContent(new ArrayList<ProviderTypeDto>());
        given(modelMapper.map(any(), any())).willReturn(mappedProviders);
        // when
        PaginationRecord<ProviderTypeDto> findResults = providerTypeService
                .findProviderTypes(PageRequest.of(0, 1));

        // then
        assertEquals(0, findResults.getContent().size());
        verify(modelMapper, times(1)).map(any(), eq(new PaginationRecord<ProviderTypeDto>().getClass()));
        verify(mockRepository, times(1)).findAll(PageRequest.of(0, 1));
    }
}