package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.model.GeneralSetting;
import com.sap.fsm.knowledgebase.domain.repository.GeneralSettingRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Unit
public class GeneralSettingServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GeneralSettingRepository mockRepository;

    @InjectMocks
    private GeneralSettingService generalSettingService;

    private static GeneralSetting fakeSetting;
    private static GeneralSettingDto requestDto;
    private static List<GeneralSetting> fakeSettingList;
    private static String someKey = "enabled";

    @BeforeAll
    public static void beforeAll() {
        fakeSetting = new GeneralSetting();
        fakeSetting.setKey(someKey);
        fakeSetting.setValue("false");
        fakeSetting.setLastChanged(new Date());

        fakeSettingList = new ArrayList<GeneralSetting>();
        fakeSettingList.add(fakeSetting);
    }

    @BeforeEach
    public void initMocks() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requestDto =  new GeneralSettingDto();
        requestDto.setKey(fakeSetting.getKey());
        requestDto.setValue(fakeSetting.getValue());
        requestDto.setLastChanged(fakeSetting.getLastChanged());
    }

    @DisplayName("Test GeneralSettingService get setting item by key successfully")
    @Test
    void shouldGetSettingItemByKeySuccessfully() {
        // given
        given(mockRepository.findByKey(someKey)).willReturn(Optional.of(fakeSetting));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        GeneralSettingDto findResult = generalSettingService
                .findBySettingKey(someKey);

        // then
        assertEquals(fakeSetting.getKey(), findResult.getKey());
        assertEquals(fakeSetting.getValue(), findResult.getValue());
    }

    @DisplayName("Test GeneralSettingService get setting item by key fails due to resource not found")
    @Test
    void shouldGetSettingItemByKeyNotFound() {
        given(mockRepository.findByKey(someKey)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            generalSettingService.findBySettingKey(someKey);
        });
    }

    @DisplayName("Test GeneralSettingService update setting item successfully")
    @Test
    void shouldUpdateSettingItemSuccessfully() {
        // given  
        requestDto.setValue("Hello");
        given(mockRepository.findByKey(someKey))
                .willReturn(Optional.of(fakeSetting));
        given(modelMapper.map(any(), any())).willReturn(requestDto);

        // when
        GeneralSettingDto updateResult = generalSettingService
                .updateBySettingKey(someKey, requestDto);

        // then
        assertEquals(requestDto.getValue(), updateResult.getValue());
        assertEquals(fakeSetting.getKey(), updateResult.getKey());
    }

    @DisplayName("Test GeneralSettingService update setting item fails due to resource not found")
    @Test
    void shouldUpdateSettingItemFailsNotFound() {
        given(mockRepository.findByKey(someKey)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            generalSettingService.updateBySettingKey(someKey, requestDto);
        });
    }

    @DisplayName("Test GeneralSettingService create setting item successfully")
    @Test
    void shouldCreateSettingItemSuccessfully() {
        // given
        given(mockRepository.findByKey(requestDto.getKey()))
                .willReturn(Optional.empty());
        given(mockRepository.save(any()))
                .willReturn(fakeSetting);
        given(modelMapper.map(any(), eq(GeneralSetting.class))).willReturn(fakeSetting);
        given(modelMapper.map(any(), eq(GeneralSettingDto.class))).willReturn(requestDto);
        // when
        GeneralSettingDto saveResult = generalSettingService
                .createKnowledgeGeneralSetting(requestDto);

        // then
        assertEquals(fakeSetting.getValue(), saveResult.getValue());
        assertEquals(fakeSetting.getKey(), saveResult.getKey());
    }

    @DisplayName("Test GeneralSettingService create setting item fails due to key duplication")
    @Test
    void shouldCreateSettingItemFailsConflict() {
        given(mockRepository.findByKey(requestDto.getKey())).willReturn(Optional.of(fakeSetting));
        Assertions.assertThrows(SettingPresentException.class, () -> {
            generalSettingService.createKnowledgeGeneralSetting(requestDto);
        });
    }

    @DisplayName("Test GeneralSettingService return setting item list as empty list successfully")
    @Test
    void shouldGetSettingItemsAsEmptyListSuccessfully() {
        // given
        given(mockRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<GeneralSetting>(new ArrayList<GeneralSetting>()));
        PaginationRecord<GeneralSettingDto> mappedGeneralSettings = new PaginationRecord<GeneralSettingDto>();
        mappedGeneralSettings.setContent(new ArrayList<GeneralSettingDto>());
        given(modelMapper.map(any(), any())).willReturn(mappedGeneralSettings);
        // when
        PaginationRecord<GeneralSettingDto> findResults = generalSettingService
                .findGeneralSettings( PageRequest.of(0,1));

        // then
        assertEquals(0, findResults.getContent().size());
        verify(modelMapper, times(1)).map(any(), eq(new PaginationRecord<GeneralSettingDto>().getClass()));
        verify(mockRepository, times(1)).findAll(PageRequest.of(0,1));
    }
}