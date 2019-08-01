package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exceptions.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
@Validated
public class KnowledgeBaseConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseConfigurationService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseProviderTypeRepository knowledgeBaseProviderTypeRepository;

    @Transactional
    public KnowledgeBaseProviderTypeDto createKnowledgeBaseProviderType(KnowledgeBaseProviderTypeDto requestDto) {
        List<KnowledgeBaseProviderType> providerTypeWithSameCode = knowledgeBaseProviderTypeRepository
                .findByCode(requestDto.getCode());
        if (providerTypeWithSameCode != null && !providerTypeWithSameCode.isEmpty()) {
            throw new ProviderTypeCodePresentException(String.format(
                    "ProviderType already present for code: %s, please use new code to create", requestDto.getCode()));
        }

        final KnowledgeBaseProviderType providerType = modelMapper.map(requestDto, KnowledgeBaseProviderType.class);
        final KnowledgeBaseProviderType savedProviderType = knowledgeBaseProviderTypeRepository.save(providerType);
        return modelMapper.map(savedProviderType, KnowledgeBaseProviderTypeDto.class);
    }

    public KnowledgeBaseProviderTypeDto findByKnowledgeBaseProviderTypeId(UUID id) {
        Optional<KnowledgeBaseProviderType> providerType = knowledgeBaseProviderTypeRepository.findById(id);
        if (providerType.isPresent()) {
            return modelMapper.map(providerType.get(), KnowledgeBaseProviderTypeDto.class);
        }
        return null;
    }

    public Page<KnowledgeBaseProviderTypeDto> findByKnowledgeBaseProviderTypes(Pageable pageable) {
        logger.info(" page = " + pageable.getPageNumber() + " pageSize = " + pageable.getPageSize());
        Page<KnowledgeBaseProviderTypeDto> providerTypes = knowledgeBaseProviderTypeRepository.findAll(pageable)
                                                            .map(item -> {
                                                              return modelMapper.map(item, KnowledgeBaseProviderTypeDto.class);  
                                                            });
       
        return providerTypes;
    }

    @Transactional
    public KnowledgeBaseProviderTypeDto updateByKnowledgeBaseProviderTypeId(UUID id, KnowledgeBaseProviderTypeDto requestDto) {
        final KnowledgeBaseProviderType providerType = modelMapper.map(requestDto, KnowledgeBaseProviderType.class);
        providerType.setId(id);
        final KnowledgeBaseProviderType savedProviderType = knowledgeBaseProviderTypeRepository.save(providerType);
        return modelMapper.map(savedProviderType, KnowledgeBaseProviderTypeDto.class);    
    }
}