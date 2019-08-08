package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class KnowledgeBaseConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseConfigurationService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseProviderTypeRepository knowledgeBaseProviderTypeRepository;

    @Transactional
    public KnowledgeBaseProviderTypeDto createKnowledgeBaseProviderType(KnowledgeBaseProviderTypeDto requestDto) {
        List<KnowledgeBaseProviderType> findResults = knowledgeBaseProviderTypeRepository
                .findByCode(requestDto.getCode());
        if (findResults != null && !findResults.isEmpty()) {
            throw new ProviderTypeCodePresentException(requestDto.getCode());
        }
        final KnowledgeBaseProviderType providerType = modelMapper.map(requestDto, KnowledgeBaseProviderType.class);
        final KnowledgeBaseProviderType savedProviderType = knowledgeBaseProviderTypeRepository.save(providerType);
        return modelMapper.map(savedProviderType, KnowledgeBaseProviderTypeDto.class);
    }

    @Transactional
    public KnowledgeBaseProviderTypeDto updateByKnowledgeBaseProviderTypeId(UUID id,
            KnowledgeBaseProviderTypeDto requestDto) {
        Optional<KnowledgeBaseProviderType> findResult = knowledgeBaseProviderTypeRepository.findByIdAndCode(id,
                requestDto.getCode());
        if (!findResult.isPresent()) {
            throw new ProviderTypeNotExistException(id);
        }
        KnowledgeBaseProviderType providerType = findResult.get();
        providerType.setName(requestDto.getName()); // only allow to change name
        // final KnowledgeBaseProviderType savedProviderType =
        // knowledgeBaseProviderTypeRepository.save(providerType);
        return modelMapper.map(providerType, KnowledgeBaseProviderTypeDto.class);
    }

    public KnowledgeBaseProviderTypeDto findByKnowledgeBaseProviderTypeId(UUID id) {
        Optional<KnowledgeBaseProviderType> findResult = knowledgeBaseProviderTypeRepository.findById(id);
        if (!findResult.isPresent()) {
            throw new ProviderTypeNotExistException(id);
        }
        return modelMapper.map(findResult.get(), KnowledgeBaseProviderTypeDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<KnowledgeBaseProviderTypeDto> findKnowledgeBaseProviderTypes(Pageable pageable) {
        Page<KnowledgeBaseProviderTypeDto> findResults = knowledgeBaseProviderTypeRepository.findAll(pageable)
                .map(item -> {
                    return modelMapper.map(item, KnowledgeBaseProviderTypeDto.class);
                });
        return modelMapper.map(findResults, new PaginationRecord<KnowledgeBaseProviderTypeDto>().getClass());
    }
}