package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.ProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.ProviderConfiguration;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.knowledgebase.domain.repository.ProviderConfigurationRepository;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;

import java.util.Date;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProviderConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(ProviderConfigurationService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProviderConfigurationRepository providerConfigurationRepository;

    @Autowired
    private ProviderTypeRepository providerTypeRepository;

    @Transactional
    public ProviderConfigurationDto createProviderConfiguration(
            ProviderConfigurationDto requestDto) {
        providerTypeRepository
                .findById(requestDto.getProviderType())
                .orElseThrow(() -> new ResourceNotExistException(requestDto.getId().toString()));

        providerConfigurationRepository
                .findByProviderType(requestDto.getProviderType()).ifPresent(result -> {
            throw new ProviderConfigurationPresentException(result.getProviderType().toString());
        });

        ProviderConfiguration config = modelMapper.map(requestDto,
                ProviderConfiguration.class);
        ProviderConfiguration savedConfig = providerConfigurationRepository.save(config);
        return modelMapper.map(savedConfig, ProviderConfigurationDto.class);
    }

    @Transactional
    public ProviderConfigurationDto updateProviderConfiguration(UUID id,
            ProviderConfigurationDto requestDto) {
        providerTypeRepository.findById(requestDto.getProviderType())
                .orElseThrow(() -> new ResourceNotExistException(requestDto.getProviderType().toString()));

        ProviderConfiguration config = providerConfigurationRepository
                .findById(id).orElseThrow(() -> 
                    new ResourceNotExistException(id.toString())
        );

        config.setAdapterAuthType(requestDto.getAdapterAuthType());
        config.setAdapterURL(requestDto.getAdapterURL());
        config.setAdapterCredential(requestDto.getAdapterCredential());
        config.setIsActive(requestDto.getIsActive());
        config.setSiteAuthType(requestDto.getSiteAuthType());
        config.setSiteURL(requestDto.getSiteURL());
        config.setSiteCredential(requestDto.getSiteCredential());
        config.setLastChanged(new Date());
        return modelMapper.map(config, ProviderConfigurationDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<ProviderConfigurationDto> findProviderConfigurations(Pageable pageable) {
        Page<ProviderConfiguration> findResults = providerConfigurationRepository.findAll(pageable);
        Page<ProviderConfigurationDto> transformedFindResults = findResults.map(item -> {
            return modelMapper.map(item, ProviderConfigurationDto.class);
        });
        return modelMapper.map(transformedFindResults,
                new PaginationRecord<ProviderConfigurationDto>().getClass());
    }

    public ProviderConfigurationDto findProviderConfigurationsByProviderTypeCode(
            String providerTypeCode) {
        ProviderType providerType = providerTypeRepository.findByCode(providerTypeCode)
                .orElseThrow(() -> new ResourceNotExistException(providerTypeCode));
        ProviderConfiguration config = providerConfigurationRepository
                .findByProviderType(providerType.getId())
                .orElseThrow(()->new ProviderConfigurationPresentException(providerTypeCode));
        return modelMapper.map(config, ProviderConfigurationDto.class);
    }
}