package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderConfiguration;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderConfigurationRepository;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderTypeRepository;
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
public class KnowledgeBaseProviderConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseProviderConfigurationService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseProviderConfigurationRepository knowledgeBaseProviderConfigurationRepository;

    @Autowired
    private KnowledgeBaseProviderTypeRepository knowledgeBaseProviderTypeRepository;

    @Transactional
    public KnowledgeBaseProviderConfigurationDto createKnowledgeBaseProviderConfiguration(
            KnowledgeBaseProviderConfigurationDto requestDto) {
        knowledgeBaseProviderTypeRepository
                .findById(requestDto.getProviderType())
                .orElseThrow(() -> new ResourceNotExistException(requestDto.getId().toString()));

        knowledgeBaseProviderConfigurationRepository
                .findByProviderType(requestDto.getProviderType()).ifPresent(result -> {
            throw new ProviderConfigurationPresentException(result.getProviderType().toString());
        });

        KnowledgeBaseProviderConfiguration config = modelMapper.map(requestDto,
                KnowledgeBaseProviderConfiguration.class);
        KnowledgeBaseProviderConfiguration savedConfig = knowledgeBaseProviderConfigurationRepository.save(config);
        return modelMapper.map(savedConfig, KnowledgeBaseProviderConfigurationDto.class);
    }

    @Transactional
    public KnowledgeBaseProviderConfigurationDto updateKnowledgeBaseProviderConfiguration(UUID id,
            KnowledgeBaseProviderConfigurationDto requestDto) {
        knowledgeBaseProviderTypeRepository.findById(requestDto.getProviderType())
                .orElseThrow(() -> new ResourceNotExistException(requestDto.getProviderType().toString()));

        KnowledgeBaseProviderConfiguration config = knowledgeBaseProviderConfigurationRepository
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
        return modelMapper.map(config, KnowledgeBaseProviderConfigurationDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<KnowledgeBaseProviderConfigurationDto> findKnowledgeBaseProviderConfigurations(Pageable pageable) {
        Page<KnowledgeBaseProviderConfiguration> findResults = knowledgeBaseProviderConfigurationRepository.findAll(pageable);
        Page<KnowledgeBaseProviderConfigurationDto> transformedFindResults = findResults.map(item -> {
            return modelMapper.map(item, KnowledgeBaseProviderConfigurationDto.class);
        });
        return modelMapper.map(transformedFindResults,
                new PaginationRecord<KnowledgeBaseProviderConfigurationDto>().getClass());
    }

    public KnowledgeBaseProviderConfigurationDto findKnowledgeBaseProviderConfigurationsByProviderTypeCode(
            String providerTypeCode) {
        KnowledgeBaseProviderType providerType = knowledgeBaseProviderTypeRepository.findByCode(providerTypeCode)
                .orElseThrow(() -> new ResourceNotExistException(providerTypeCode));
        KnowledgeBaseProviderConfiguration config = knowledgeBaseProviderConfigurationRepository
                .findByProviderType(providerType.getId())
                .orElseThrow(()->new ProviderConfigurationPresentException(providerTypeCode));
        return modelMapper.map(config, KnowledgeBaseProviderConfigurationDto.class);
    }
}