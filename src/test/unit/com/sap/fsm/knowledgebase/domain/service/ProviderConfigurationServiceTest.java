package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.model.ProviderConfiguration;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.knowledgebase.domain.repository.ProviderConfigurationRepository;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.OtherActiveConfigurationPresentException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderConfigurationPresentException;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.dto.ProviderConfigurationDto;
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
public class ProviderConfigurationServiceTest {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProviderTypeRepository mockProviderTypeRepository;

    @Mock
    private ProviderConfigurationRepository mockProviderConfigurationRepository;

    @InjectMocks
    private ProviderConfigurationService providerConfigurationService;

    private static ProviderConfiguration fakeProviderConfig;
    private static ProviderConfigurationDto requestDto;
    private static List<ProviderConfiguration> fakeProviderConfigList;
    private static UUID someId = UUID.fromString("6f6c1b6e-0520-4a27-b37f-f34be2d964bf");
    private static UUID providerTypeId = UUID.fromString("792e87db-f234-4d5f-9aac-b6dcb5980a88");

    @BeforeAll
    public static void beforeAll() {
        fakeProviderConfig = new ProviderConfiguration();
        fakeProviderConfig.setId(someId);
        fakeProviderConfig.setIsActive(true);
        fakeProviderConfig.setLastChanged(new Date());
        fakeProviderConfig.setProviderType(providerTypeId);
        fakeProviderConfig.setAdapterAuthType("Basic");
        fakeProviderConfig.setAdapterURL("https://sapdemo-responsive.mindtouch.us");

        fakeProviderConfigList = new ArrayList<ProviderConfiguration>();
        fakeProviderConfigList.add(fakeProviderConfig);
    }

    @BeforeEach
    public void initMocks() throws JSONException {
        MockitoAnnotations.initMocks(this);
        requestDto = new ProviderConfigurationDto();
        requestDto.setId(fakeProviderConfig.getId());
        requestDto.setIsActive(fakeProviderConfig.getIsActive());
        requestDto.setLastChanged(fakeProviderConfig.getLastChanged());
        requestDto.setProviderType(fakeProviderConfig.getProviderType());
        requestDto.setAdapterAuthType(fakeProviderConfig.getAdapterAuthType());
        requestDto.setAdapterURL(fakeProviderConfig.getAdapterURL());
    }

    @DisplayName("Test ProviderConfigurationService get provider configuration by providertype code successfully")
    @Test
    void shouldGetProviderConfigurationByTypeCodeSuccessfully() {
        // given
        ProviderType providerType = new ProviderType();
        providerType.setId(providerTypeId);
        given(mockProviderTypeRepository.findByCode("MindTouch")).willReturn(Optional.of(providerType));
        given(mockProviderConfigurationRepository.findByProviderType(providerTypeId)).willReturn(Optional.of(fakeProviderConfig));
        given(modelMapper.map(any(), eq(ProviderConfigurationDto.class))).willReturn(requestDto);

        // when
        ProviderConfigurationDto findResult = providerConfigurationService
                .findProviderConfigurationsByProviderTypeCode("MindTouch");

        // then
        assertEquals(fakeProviderConfig.getId(), findResult.getId());
        assertEquals(fakeProviderConfig.getProviderType(), findResult.getProviderType());
    }

    @DisplayName("Test ProviderConfigurationService get provider configuration by providertype code fails due to type code invalid")
    @Test
    void shouldGetProviderConfigurationByTypeCodeFailsCodeInvalid() {
        // given
        given(mockProviderTypeRepository.findByCode("MindTouch")).willReturn(Optional.empty());
    
        // when

        // then
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerConfigurationService.findProviderConfigurationsByProviderTypeCode("MindTouch");
        });
    }

    @DisplayName("Test ProviderConfigurationService get provider configuration by providertype code fails due to config not exist")
    @Test
    void shouldGetProviderConfigurationByTypeCodeFailsConfigNonExist() {
        // given
        ProviderType providerType = new ProviderType();
        providerType.setId(providerTypeId);
        given(mockProviderTypeRepository.findByCode("MindTouch")).willReturn(Optional.of(providerType));
        given(mockProviderConfigurationRepository.findByProviderType(providerTypeId)).willReturn(Optional.empty());
    
        // when

        // then
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerConfigurationService.findProviderConfigurationsByProviderTypeCode("MindTouch");
        });
    }

    @DisplayName("Test ProviderConfigurationService create provider configuration successfully")
    @Test
    void shouldCreateProviderConfigurationSuccessfully() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.of(new ProviderType()));
        given(mockProviderConfigurationRepository.findByProviderType(any())).willReturn(Optional.empty());
        given(mockProviderConfigurationRepository.existsByIsActive(any())).willReturn(false);
        given(modelMapper.map(any(), eq(ProviderConfiguration.class))).willReturn(fakeProviderConfig);
        given(modelMapper.map(any(), eq(ProviderConfigurationDto.class))).willReturn(requestDto);
        given(mockProviderConfigurationRepository.save(any())).willReturn(fakeProviderConfig);
        // when
        ProviderConfigurationDto saveResult = providerConfigurationService
                .createProviderConfiguration(requestDto);

        // then
        assertEquals(fakeProviderConfig.getProviderType(), saveResult.getProviderType());
        assertEquals(fakeProviderConfig.getId(), saveResult.getId());
    }

    @DisplayName("Test ProviderConfigurationService create provider configuration fails due to provider type invalid")
    @Test
    void shouldCreateProviderConfigurationFailsProviderTypeInvalid() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.empty());
       
        // when

        // then
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerConfigurationService
                .createProviderConfiguration(requestDto);
        });
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    @DisplayName("Test ProviderConfigurationService create provider configuration fails due to provider configuration present")
    @Test
    void shouldCreateProviderConfigurationFailsProviderConfigurationPresent() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.of(new ProviderType()) );
        given(mockProviderConfigurationRepository.findByProviderType(requestDto.getProviderType())).willReturn(Optional.of(fakeProviderConfig) );
        // when

        // then
        Assertions.assertThrows(ProviderConfigurationPresentException.class, () -> {
            providerConfigurationService
                .createProviderConfiguration(requestDto);
        });
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    @DisplayName("Test ProviderConfigurationService create provider configuration fails due to another provider configuration activated")
    @Test
    void shouldCreateProviderConfigurationFailsOtherConfigurationActivated() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.of(new ProviderType()) );
        given(mockProviderConfigurationRepository.findByProviderType(requestDto.getProviderType())).willReturn(Optional.empty() );
        given(mockProviderConfigurationRepository.existsByIsActive(true)).willReturn(true );
        // when

        // then
        Assertions.assertThrows(OtherActiveConfigurationPresentException.class, () -> {
            providerConfigurationService
                .createProviderConfiguration(requestDto);
        });
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    
    @DisplayName("Test ProviderConfigurationService update provider configuration fails due to provider type code invalid")
    @Test
    void shouldUpdateProviderConfigurationFailsProviderTypeCodeInvalid() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.empty()) ;
        // when

        // then
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerConfigurationService
                .updateProviderConfiguration(requestDto.getId(), requestDto);
        });
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    @DisplayName("Test ProviderConfigurationService update provider configuration fails due to configuration not present")
    @Test
    void shouldUpdateProviderConfigurationFailsProviderConfigurationNotPresent() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.of(new ProviderType())) ;
        given(mockProviderConfigurationRepository.findById(providerTypeId)).willReturn(Optional.empty() );
        
        // when

        // then
        Assertions.assertThrows(ResourceNotExistException.class, () -> {
            providerConfigurationService
                .updateProviderConfiguration(requestDto.getId(), requestDto);
        });
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    @DisplayName("Test ProviderConfigurationService update provider configuration successfully")
    @Test
    void shouldUpdateProviderConfigurationSuccessfully() {
        // given
        given(mockProviderTypeRepository.findById(requestDto.getProviderType())).willReturn(Optional.of(new ProviderType())) ;
        given(mockProviderConfigurationRepository.findById(any())).willReturn(Optional.of(fakeProviderConfig) );
        given(mockProviderConfigurationRepository.existsByIsActive(true)).willReturn(false );
        given(modelMapper.map(any(), eq(ProviderConfigurationDto.class))).willReturn(requestDto);
        // when
        ProviderConfigurationDto savedConfig = providerConfigurationService.updateProviderConfiguration(requestDto.getId(), requestDto);
        // then
        assertEquals(fakeProviderConfig.getProviderType(), savedConfig.getProviderType());
        assertEquals(fakeProviderConfig.getId(), savedConfig.getId());
        verify(mockProviderConfigurationRepository, times(0)).save(any());
    }

    @DisplayName("Test ProviderConfigurationService return provider configuration list as empty list successfully")
    @Test
    void shouldGetProviderTypesAsEmptyListSuccessfully() {
        // given
        given(mockProviderConfigurationRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<ProviderConfiguration>(new ArrayList<ProviderConfiguration>()));
        PaginationRecord<ProviderConfigurationDto> mappedConfigurations = new PaginationRecord<ProviderConfigurationDto>();
        mappedConfigurations.setContent(new ArrayList<ProviderConfigurationDto>());
        given(modelMapper.map(any(), any())).willReturn(mappedConfigurations);
        // when
        PaginationRecord<ProviderConfigurationDto> findResults = providerConfigurationService
                .findProviderConfigurations(PageRequest.of(0, 1));

        // then
        assertEquals(0, findResults.getContent().size());
        verify(modelMapper, times(1)).map(any(), eq(new PaginationRecord<ProviderConfigurationDto>().getClass()));
        verify(mockProviderConfigurationRepository, times(1)).findAll(PageRequest.of(0, 1));
    }
}