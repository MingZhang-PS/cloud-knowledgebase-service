package com.sap.fsm.knowledgebase.domain.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseGeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.SettingPresentException;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseGeneralSettingRepository;

@Service
@Transactional(readOnly = true)
public class KnowledgeBaseGeneralSettingService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseGeneralSettingService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseGeneralSettingRepository knowledgeBaseGeneralSettingRepository;

    @Transactional
    public KnowledgeBaseGeneralSettingDto createKnowledgeGeneralSetting(KnowledgeBaseGeneralSettingDto requestDto) {
        knowledgeBaseGeneralSettingRepository.findByKey(requestDto.getKey()).ifPresent(result -> {
            throw new SettingPresentException(requestDto.getKey());
        });

        KnowledgeBaseGeneralSetting setting = modelMapper.map(requestDto, KnowledgeBaseGeneralSetting.class);
        KnowledgeBaseGeneralSetting savedSetting = knowledgeBaseGeneralSettingRepository.save(setting);
        return modelMapper.map(savedSetting, KnowledgeBaseGeneralSettingDto.class);
    }

    @Transactional
    public KnowledgeBaseGeneralSettingDto updateByKnowledgeBaseSettingKey(String key,
            KnowledgeBaseGeneralSettingDto requestDto) {
        KnowledgeBaseGeneralSetting findResult = knowledgeBaseGeneralSettingRepository.findByKey(requestDto.getKey())
                .orElseThrow(() -> new ResourceNotExistException(key));

        KnowledgeBaseGeneralSetting setting = findResult;
        setting.setValue(requestDto.getValue());
        setting.setLastChanged(new Date());
        return modelMapper.map(setting, KnowledgeBaseGeneralSettingDto.class);
    }

    public KnowledgeBaseGeneralSettingDto findByKnowledgeBaseSettingKey(String key) {
        KnowledgeBaseGeneralSetting findResult = knowledgeBaseGeneralSettingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotExistException(key));
        return modelMapper.map(findResult, KnowledgeBaseGeneralSettingDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<KnowledgeBaseGeneralSettingDto> findKnowledgeBaseGeneralSettings(Pageable pageable) {
        Page<KnowledgeBaseGeneralSetting> findResults = knowledgeBaseGeneralSettingRepository.findAll(pageable);
        Page<KnowledgeBaseGeneralSettingDto> transformedFindResults = findResults.map(item -> {
            return modelMapper.map(item, KnowledgeBaseGeneralSettingDto.class);
        });
        return modelMapper.map(transformedFindResults,
                new PaginationRecord<KnowledgeBaseGeneralSettingDto>().getClass());
    }
}