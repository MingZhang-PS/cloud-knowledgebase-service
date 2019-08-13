package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.ProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.dto.ProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.GeneralSettingService;
import com.sap.fsm.knowledgebase.domain.service.ProviderConfigurationService;
import com.sap.fsm.knowledgebase.domain.service.ProviderTypeService;

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
    private ProviderTypeService providerTypeService;
    @Autowired
    private GeneralSettingService generalSettingService;
    @Autowired
    private ProviderConfigurationService providerConfigurationService;

    @ApiOperation(value = "Create a provider type")
    @ApiImplicitParam(name = "requestDto", value = "Provider Type Entity", required = true, dataType = "ProviderTypeDto", paramType = "body")
    @PostMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ResponseBody
    public ProviderTypeDto createProviderType(
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.createProviderType(requestDto);
    }

    @ApiOperation(value = "Get all provider types")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderTypeDto> findProviderTypes(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return providerTypeService.findProviderTypes(PageRequest.of(page, size));
    }

    @ApiOperation(value = "Get a provider type by id")
    @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path")
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderTypeDto findProviderTypeById(@PathVariable UUID id) {
        return providerTypeService.findByProviderTypeId(id);
    }

    @ApiOperation(value = "Update a provider type by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider Type Entity", required = true, dataType = "ProviderTypeDto", paramType = "body") })
    @PutMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public ProviderTypeDto updateProviderTypeById(@PathVariable UUID id,
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.updateByProviderTypeId(id, requestDto);
    }

    @ApiOperation(value = "Create a general setting item")
    @ApiImplicitParam(name = "requestDto", value = "General Setting Item", required = true, dataType = "GeneralSettingDto", paramType = "body")
    @PostMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME)
    @ResponseBody
    public GeneralSettingDto createGeneralSetting(
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.createKnowledgeGeneralSetting(requestDto);
    }

    
    @ApiOperation(value = "Get all general setting items")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<GeneralSettingDto> findGeneralSettings(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return generalSettingService.findGeneralSettings(PageRequest.of(page, size));
    }

    @ApiOperation(value = "Update a general setting item by key")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "Key of a geneal setting item", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider General Setting Item", required = true, dataType = "GeneralSettingDto", paramType = "body") })
    @PutMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME + "/{key}")
    @ResponseBody
    public GeneralSettingDto updateGeneralSettingById(@PathVariable String key,
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.updateBySettingKey(key, requestDto);
    }

    @ApiOperation(value = "Get a general setting by key")
    @ApiImplicitParam(name = "key", value = "Key of a general setting item", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = GENERAL_SETTING_RESOURCE_URL_NAME + "/{key}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public GeneralSettingDto findGeneralSettingById(@PathVariable String key) {
        return generalSettingService.findBySettingKey(key);
    }


    @ApiOperation(value = "Create a provider configuration")
    @ApiImplicitParam(name = "requestDto", value = "Provider configuration", required = true, dataType = "ProviderConfigurationDto", paramType = "body")
    @PostMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME)
    @ResponseBody
    public ProviderConfigurationDto createProviderConfiguration(
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.createProviderConfiguration(requestDto);
    }


    @ApiOperation(value = "Get all provider configuration")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", defaultValue = "20", required = false, dataType = "int", paramType = "query") })
    @GetMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderConfigurationDto> findProviderConfiguration(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        return providerConfigurationService.findProviderConfigurations(PageRequest.of(page, size));
    }
  
    @ApiOperation(value = "Get a provider configuration by provider type code")
    @ApiImplicitParam(name = "providerType", value = "code of provider type", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME + "/{providerType}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderConfigurationDto findProviderConfigurationByProviderType(
            @PathVariable String providerType) {
        return providerConfigurationService.findProviderConfigurationsByProviderTypeCode(providerType);
    }

    @ApiOperation(value = "Update a configuration by configuration id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "identifier of a configuration item", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider Configuration Item", required = true, dataType = "ProviderConfigurationDto", paramType = "body") })
    @PutMapping(value = PROVIDER_CONFIGURATION_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public ProviderConfigurationDto updateConfigurationById(@PathVariable UUID id,
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.updateProviderConfiguration(id, requestDto);
    }
}