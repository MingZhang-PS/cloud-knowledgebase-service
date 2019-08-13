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

import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.SettingPresentException;
import com.sap.fsm.knowledgebase.domain.model.GeneralSetting;
import com.sap.fsm.knowledgebase.domain.repository.GeneralSettingRepository;

@Service
@Transactional(readOnly = true)
public class GeneralSettingService {

    private static final Logger logger = LoggerFactory.getLogger(GeneralSettingService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GeneralSettingRepository generalSettingRepository;

    @Transactional
    public GeneralSettingDto createKnowledgeGeneralSetting(GeneralSettingDto requestDto) {
        generalSettingRepository.findByKey(requestDto.getKey()).ifPresent(result -> {
            throw new SettingPresentException(requestDto.getKey());
        });

        GeneralSetting setting = modelMapper.map(requestDto, GeneralSetting.class);
        GeneralSetting savedSetting = generalSettingRepository.save(setting);
        return modelMapper.map(savedSetting, GeneralSettingDto.class);
    }

    @Transactional
    public GeneralSettingDto updateBySettingKey(String key,
            GeneralSettingDto requestDto) {
        GeneralSetting findResult = generalSettingRepository.findByKey(requestDto.getKey())
                .orElseThrow(() -> new ResourceNotExistException(key));

        GeneralSetting setting = findResult;
        setting.setValue(requestDto.getValue());
        setting.setLastChanged(new Date());
        return modelMapper.map(setting, GeneralSettingDto.class);
    }

    public GeneralSettingDto findBySettingKey(String key) {
        GeneralSetting findResult = generalSettingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotExistException(key));
        return modelMapper.map(findResult, GeneralSettingDto.class);
    }

    @SuppressWarnings("unchecked")
    public PaginationRecord<GeneralSettingDto> findGeneralSettings(Pageable pageable) {
        Page<GeneralSetting> findResults = generalSettingRepository.findAll(pageable);
        Page<GeneralSettingDto> transformedFindResults = findResults.map(item -> {
            return modelMapper.map(item, GeneralSettingDto.class);
        });
        return modelMapper.map(transformedFindResults,
                new PaginationRecord<GeneralSettingDto>().getClass());
    }
}