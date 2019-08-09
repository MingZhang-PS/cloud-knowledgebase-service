package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseGeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseGeneralSettingRepository;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.SettingPresentException;
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
public class KnowledgeBaseGeneralSettingServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KnowledgeBaseGeneralSettingRepository mockRepository;

    @InjectMocks
    private KnowledgeBaseGeneralSettingService knowledgeBaseGeneralSettingService;

    private static KnowledgeBaseGeneralSetting fakeSetting;
    private static KnowledgeBaseGeneralSettingDto requestDto;
    private static List<KnowledgeBaseGeneralSetting> fakeSettingList;
    private static UUID someId = UUID.fromString("6f6c1b6e-0520-4a27-b37f-f34be2d964bf");

    @BeforeAll
    public static void beforeAll() {
        fakeSetting = new KnowledgeBaseGeneralSetting();
        fakeSetting.setKey("enabled");
        fakeSetting.setValue("false");
        fakeSetting.setId(someId);
        fakeSetting.setLastChanged(new Date());

        fakeSettingList = new ArrayList<KnowledgeBaseGeneralSetting>();
        fakeSettingList.add(fakeSetting);
    }

    @BeforeEach
    public void initMocks() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requestDto =  new KnowledgeBaseGeneralSettingDto();
        requestDto.setKey(fakeSetting.getKey());
        requestDto.setValue(fakeSetting.getValue());
        requestDto.setId(fakeSetting.getId());
        requestDto.setLastChanged(fakeSetting.getLastChanged());
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService get setting item by id successfully")
    @Test
    void shouldGetSettingItemByIdSuccessfully() {
        // given
        given(mockRepository.findById(someId)).willReturn(Optional.of(fakeSetting));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        KnowledgeBaseGeneralSettingDto findResult = knowledgeBaseGeneralSettingService
                .findByKnowledgeBaseSettingId(someId);

        // then
        assertEquals(fakeSetting.getId(), findResult.getId());
        assertEquals(fakeSetting.getKey(), findResult.getKey());
        assertEquals(fakeSetting.getValue(), findResult.getValue());
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService get setting item by id fails due to resource not found")
    @Test
    void shouldGetSettingItemByIdNotFound() {
        given(mockRepository.findById(someId)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            knowledgeBaseGeneralSettingService.findByKnowledgeBaseSettingId(someId);
        });
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService update setting item successfully")
    @Test
    void shouldUpdateSettingItemSuccessfully() {
        // given  
        requestDto.setValue("Hello");
        given(mockRepository.findByIdAndKey(someId, requestDto.getKey()))
                .willReturn(Optional.of(fakeSetting));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        KnowledgeBaseGeneralSettingDto updateResult = knowledgeBaseGeneralSettingService
                .updateByKnowledgeBaseSettingId(someId, requestDto);

        // then
        assertEquals(requestDto.getValue(), updateResult.getValue());
        assertEquals(fakeSetting.getKey(), updateResult.getKey());
        assertEquals(fakeSetting.getId(), updateResult.getId());
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService update setting item fails due to resource not found")
    @Test
    void shouldUpdateSettingItemFailsNotFound() {
        given(mockRepository.findByIdAndKey(someId, requestDto.getKey())).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            knowledgeBaseGeneralSettingService.updateByKnowledgeBaseSettingId(someId, requestDto);
        });
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService create setting item successfully")
    @Test
    void shouldCreateSettingItemSuccessfully() {
        // given
        given(mockRepository.findByKey(requestDto.getKey()))
                .willReturn(Optional.empty());
        given(mockRepository.save(any()))
                .willReturn(fakeSetting);
        given(modelMapper.map(any(), eq(KnowledgeBaseGeneralSetting.class))).willReturn(fakeSetting);
        given(modelMapper.map(any(), eq(KnowledgeBaseGeneralSettingDto.class))).willReturn(requestDto);
        // when
        KnowledgeBaseGeneralSettingDto saveResult = knowledgeBaseGeneralSettingService
                .createKnowledgeGeneralSetting(requestDto);

        // then
        assertEquals(fakeSetting.getValue(), saveResult.getValue());
        assertEquals(fakeSetting.getKey(), saveResult.getKey());
        assertEquals(fakeSetting.getId(), saveResult.getId());
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService create setting item fails due to key duplication")
    @Test
    void shouldCreateSettingItemFailsConflict() {
        given(mockRepository.findByKey(requestDto.getKey())).willReturn(Optional.of(fakeSetting));
        Assertions.assertThrows(SettingPresentException.class, () -> {
            knowledgeBaseGeneralSettingService.createKnowledgeGeneralSetting(requestDto);
        });
    }

    @DisplayName("Test knowledgeBaseGeneralSettingService return setting item list as empty list successfully")
    @Test
    void shouldGetSettingItemsAsEmptyListSuccessfully() {
        // given
        given(mockRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<KnowledgeBaseGeneralSetting>(new ArrayList<KnowledgeBaseGeneralSetting>()));
        PaginationRecord<KnowledgeBaseGeneralSettingDto> mappedGeneralSettings = new PaginationRecord<KnowledgeBaseGeneralSettingDto>();
        mappedGeneralSettings.setContent(new ArrayList<KnowledgeBaseGeneralSettingDto>());
        given(modelMapper.map(any(), any())).willReturn(mappedGeneralSettings);
        // when
        PaginationRecord<KnowledgeBaseGeneralSettingDto> findResults = knowledgeBaseGeneralSettingService
                .findKnowledgeBaseGeneralSettings("", PageRequest.of(0,1));

        // then
        assertEquals(0, findResults.getContent().size());
        verify(modelMapper, times(1)).map(any(), eq(new PaginationRecord<KnowledgeBaseGeneralSettingDto>().getClass()));
        verify(mockRepository, times(1)).findAll(PageRequest.of(0,1));
    }
}