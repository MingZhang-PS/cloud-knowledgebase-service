package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseGeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseGeneralSettingRepository;
import com.sap.fsm.knowledgebase.domain.exception.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antlr.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class KnowledgeBaseGeneralSettingService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseGeneralSettingService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseGeneralSettingRepository knowledgeBaseGeneralSettingRepository;

    @Transactional
    public KnowledgeBaseGeneralSettingDto createKnowledgeGeneralSetting(KnowledgeBaseGeneralSettingDto requestDto) {
        Optional<KnowledgeBaseGeneralSetting> findResult = knowledgeBaseGeneralSettingRepository.findByKey(requestDto.getKey());
        if (findResult.isPresent()) {
            throw new SettingPresentException(requestDto.getKey());
        }
        KnowledgeBaseGeneralSetting setting = modelMapper.map(requestDto, KnowledgeBaseGeneralSetting.class);
        KnowledgeBaseGeneralSetting savedSetting = knowledgeBaseGeneralSettingRepository.save(setting);
        return modelMapper.map(savedSetting, KnowledgeBaseGeneralSettingDto.class);
    }

    @Transactional
    public KnowledgeBaseGeneralSettingDto updateByKnowledgeBaseSettingId(UUID id,
    KnowledgeBaseGeneralSettingDto requestDto) {
        Optional<KnowledgeBaseGeneralSetting> findResult = knowledgeBaseGeneralSettingRepository.findByIdAndKey(id,
                requestDto.getKey());
        if (!findResult.isPresent()) {
            throw new ResourceNotExistException(id);
        }
        KnowledgeBaseGeneralSetting setting = findResult.get();
        setting.setValue(requestDto.getValue()); 
        return modelMapper.map(setting, KnowledgeBaseGeneralSettingDto.class);
    }

    public KnowledgeBaseGeneralSettingDto findByKnowledgeBaseSettingId(UUID id) {
        Optional<KnowledgeBaseGeneralSetting> findResult = knowledgeBaseGeneralSettingRepository.findById(id);
        if (!findResult.isPresent()) {
            throw new ResourceNotExistException(id);
        }
        return modelMapper.map(findResult.get(), KnowledgeBaseGeneralSettingDto.class);
    }
/* 
    @SuppressWarnings("unchecked")
    public PaginationRecord<KnowledgeBaseGeneralSettingDto> findKnowledgeBaseGeneralSettings(String key, Pageable pageable) {
        if(key != null && !key.isEmpty()) {

        }
        Page<KnowledgeBaseGeneralSettingDto> findResults = knowledgeBaseGeneralSettingRepository.findByKey(pageable)
                .map(item -> {
                    return modelMapper.map(item, KnowledgeBaseGeneralSettingDto.class);
                });
        return modelMapper.map(findResults, new PaginationRecord<KnowledgeBaseGeneralSettingDto>().getClass());
    } */
}