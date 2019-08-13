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
import java.util.UUID;
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
    private static UUID someId = UUID.fromString("6f6c1b6e-0520-4a27-b37f-f34be2d964bf");

    @BeforeAll
    public static void beforeAll() {
        fakeType = new ProviderType();
        fakeType.setCode("MindTouch");
        fakeType.setName("test");
        fakeType.setId(someId);
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
        requestDto.setId(fakeType.getId());
        requestDto.setLastChanged(fakeType.getLastChanged());
    }

    @DisplayName("Test ProviderTypeService get provider type by id successfully")
    @Test
    void shouldGetProviderTypeByIdSuccessfully() {
        // given
        given(mockRepository.findById(someId)).willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        ProviderTypeDto findResult = providerTypeService
                .findByProviderTypeId(someId);

        // then
        assertEquals(fakeType.getCode(), findResult.getCode());
        assertEquals(fakeType.getId(), findResult.getId());
        assertEquals(fakeType.getName(), findResult.getName());
    }

    @DisplayName("Test ProviderTypeService get provider type by id fails due to resource not found")
    @Test
    void shouldGetProviderTypeByIdNotFound() {
        given(mockRepository.findById(someId)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerTypeService.findByProviderTypeId(someId);
        });
    }

    @DisplayName("Test ProviderTypeService update provider type successfully")
    @Test
    void shouldUpdateProviderTypeSuccessfully() {
        // given
        requestDto.setName("Hello");
        given(mockRepository.findByIdAndCode(someId, requestDto.getCode())).willReturn(Optional.of(fakeType));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        ProviderTypeDto updateResult = providerTypeService
                .updateByProviderTypeId(someId, requestDto);

        // then
        assertEquals(requestDto.getName(), updateResult.getName());
        assertEquals(fakeType.getCode(), updateResult.getCode());
        assertEquals(fakeType.getId(), updateResult.getId());
    }

    @DisplayName("Test ProviderTypeService update provider type fails due to resource not found")
    @Test
    void shouldUpdateProviderTypeFailsNotFound() {
        given(mockRepository.findByIdAndCode(someId, requestDto.getCode())).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerTypeService.updateByProviderTypeId(someId, requestDto);
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
        assertEquals(fakeType.getId(), saveResult.getId());
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