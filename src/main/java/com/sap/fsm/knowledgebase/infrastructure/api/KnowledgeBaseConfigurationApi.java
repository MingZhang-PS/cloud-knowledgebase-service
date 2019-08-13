package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseGeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderConfiguration;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseGeneralSettingService;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseProviderConfigurationService;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseProviderTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageRequest;

import io.swagger.annotations.*;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Type API", tags = { "KnowledgeBase Provider Type API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseConfigurationApi {
    private static final String PROVIDER_TYPE_RESOURCE_URL_NAME = "provider-types";
    private static final String GENERAL_SETTING_RESOURCE_URL_NAME = "general-settings";
    private static final String PROVIDER_CONFIGURATION_RESOURCE_URL_NAME = "provider-configurations";

    @Autowired
    private KnowledgeBaseProviderTypeService knowledgeBaseProviderTypeService;
    @Autowired
    private KnowledgeBaseGeneralSettingService knowledgeBaseGeneralSettingService;
    @Autowired
    private KnowledgeBaseProviderConfigurationService knowledgeBaseConfigurationService;

    @ApiOperation(value = "Create a knowledgebase provider type")
    @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider Type Entity", required = true, dataType = "KnowledgeBaseProviderTypeDto", paramType = "body")
    @PostMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ResponseBody
    public KnowledgeBaseProviderTypeDto createKnowledgeBaseProviderType(
            @Validated @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return knowledgeBaseProviderTypeService.createKnowledgeBaseProviderType(requestDto);
    }

    @ApiOperation(value = "Get all knowledgebase provider type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseProviderTypeDto> findKnowledgeBaseProviderTypes(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return knowledgeBaseProviderTypeService.findKnowledgeBaseProviderTypes(PageRequest.of(page, size));
    }

    @ApiOperation(value = "Get a knowledgebase provider type by id")
    @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path")
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public KnowledgeBaseProviderTypeDto findKnowledgeBaseProviderTypeById(@PathVariable UUID id) {
        return knowledgeBaseProviderTypeService.findByKnowledgeBaseProviderTypeId(id);
    }

    @ApiOperation(value = "Update a knowledgebase provider type by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider Type Entity", required = true, dataType = "KnowledgeBaseProviderTypeDto", paramType = "body") })
    @PutMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public KnowledgeBaseProviderTypeDto updateKnowledgeBaseProviderTypeById(@PathVariable UUID id,
            @Validated @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return knowledgeBaseProviderTypeService.updateByKnowledgeBaseProviderTypeId(id, requestDto);
    }

    @ApiOperation(value = "Create a knowledgebase general setting item")
    @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase General Setting Item", required = true, dataType = "KnowledgeBaseGeneralSettingDto", paramType = "body")
    @PostMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME)
    @ResponseBody
    public KnowledgeBaseGeneralSettingDto createKnowledgeBaseGeneralSetting(
            @Validated @RequestBody KnowledgeBaseGeneralSettingDto requestDto) {
        return knowledgeBaseGeneralSettingService.createKnowledgeGeneralSetting(requestDto);
    }

    
    @ApiOperation(value = "Get all knowledgebase general setting items")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseGeneralSettingDto> findKnowledgeBaseGeneralSettings(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return knowledgeBaseGeneralSettingService.findKnowledgeBaseGeneralSettings(PageRequest.of(page, size));
    }

    @ApiOperation(value = "Update a knowledgebase general setting item by key")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "Key of a geneal setting item", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider General Setting Item", required = true, dataType = "KnowledgeBaseGeneralSettingDto", paramType = "body") })
    @PutMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME + "/{key}")
    @ResponseBody
    public KnowledgeBaseGeneralSettingDto updateKnowledgeBaseGeneralSettingById(@PathVariable String key,
            @Validated @RequestBody KnowledgeBaseGeneralSettingDto requestDto) {
        return knowledgeBaseGeneralSettingService.updateByKnowledgeBaseSettingKey(key, requestDto);
    }

    @ApiOperation(value = "Get a knowledgebase general setting by key")
    @ApiImplicitParam(name = "key", value = "Key of a general setting item", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME + "/{key}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public KnowledgeBaseGeneralSettingDto findKnowledgeBaseGeneralSettingById(@PathVariable String key) {
        return knowledgeBaseGeneralSettingService.findByKnowledgeBaseSettingKey(key);
    }


    @ApiOperation(value = "Create a knowledgebase provider configuration")
    @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase provider configuration", required = true, dataType = "KnowledgeBaseProviderConfigurationDto", paramType = "body")
    @PostMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME)
    @ResponseBody
    public KnowledgeBaseProviderConfigurationDto createKnowledgeBaseProviderConfiguration(
            @Validated @RequestBody KnowledgeBaseProviderConfigurationDto requestDto) {
        return knowledgeBaseConfigurationService.createKnowledgeBaseProviderConfiguration(requestDto);
    }


    @ApiOperation(value = "Get all knowledgebase provider configuration")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseProviderConfigurationDto> findKnowledgeBaseProviderConfiguration(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return knowledgeBaseConfigurationService.findKnowledgeBaseProviderConfigurations(PageRequest.of(page, size));
    }
  
    @ApiOperation(value = "Get a knowledgebase provider configuration by provider type code")
    @ApiImplicitParam(name = "providerType", value = "code of provider type", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME + "/{providerType}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public KnowledgeBaseProviderConfigurationDto findKnowledgeBaseProviderConfigurationByProviderType(
            @PathVariable String providerType) {
        return knowledgeBaseConfigurationService.findKnowledgeBaseProviderConfigurationsByProviderTypeCode(providerType);
    }

    @ApiOperation(value = "Update a knowledgebase configuration by configuration id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "identifier of a configuration item", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider Configuration Item", required = true, dataType = "KnowledgeBaseProviderConfigurationDto", paramType = "body") })
    @PutMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public KnowledgeBaseProviderConfigurationDto updateKnowledgeBaseConfigurationById(@PathVariable UUID id,
            @Validated @RequestBody KnowledgeBaseProviderConfigurationDto requestDto) {
        return knowledgeBaseConfigurationService.updateKnowledgeBaseProviderConfiguration(id, requestDto);
    }
}