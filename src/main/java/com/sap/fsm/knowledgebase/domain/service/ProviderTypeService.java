package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.ProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.knowledgebase.domain.exception.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class ProviderTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ProviderTypeService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProviderTypeRepository providerTypeRepository;

    @Transactional
    public ProviderTypeDto createProviderType(ProviderTypeDto requestDto) {
        providerTypeRepository.findByCode(requestDto.getCode()).ifPresent(result -> {
            throw new ProviderTypePresentException(result.getCode());
        });

        ProviderType providerType = modelMapper.map(requestDto, ProviderType.class);
        ProviderType savedProviderType = providerTypeRepository.save(providerType);
        return modelMapper.map(savedProviderType, ProviderTypeDto.class);
    }

    @Transactional
    public ProviderTypeDto updateByProviderTypeCode(String code,
            ProviderTypeDto requestDto) {
        ProviderType findResult = providerTypeRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotExistException(code));

        ProviderType providerType = findResult;
        providerType.setName(requestDto.getName());
        providerType.setLastChanged(new Date()); // update field lastChanged to increase version
        return modelMapper.map(providerType, ProviderTypeDto.class);
    }

    public ProviderTypeDto findByProviderTypeCode(String code) {
        ProviderType findResult = providerTypeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotExistException(code));
        return modelMapper.map(findResult, ProviderTypeDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<ProviderTypeDto> findProviderTypes(Pageable pageable) {
        Page<ProviderTypeDto> findResults = providerTypeRepository.findAll(pageable)
                .map(item -> {
                    return modelMapper.map(item, ProviderTypeDto.class);
                });
        return modelMapper.map(findResults, new PaginationRecord<ProviderTypeDto>().getClass());
    }
}