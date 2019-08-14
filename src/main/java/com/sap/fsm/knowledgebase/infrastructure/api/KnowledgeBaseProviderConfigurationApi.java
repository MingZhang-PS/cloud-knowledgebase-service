package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.ProviderConfigurationDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.ProviderConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;

import io.swagger.annotations.*;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Configuration API", tags = { "KnowledgeBase Provider Configuration API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/provider-configurations", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseProviderConfigurationApi {
    @Autowired
    private ProviderConfigurationService providerConfigurationService;


    @ApiOperation(value = "Create a provider configuration")
    @ApiImplicitParam(name = "requestDto", value = "Provider configuration", required = true, dataType = "ProviderConfigurationDto", paramType = "body")
    @PostMapping
    @ResponseBody
    public ProviderConfigurationDto createProviderConfiguration(
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.createProviderConfiguration(requestDto);
    }


    @ApiOperation(value = "Get all provider configuration")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping( consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderConfigurationDto> findProviderConfiguration(
         Pageable pageable) {
        return providerConfigurationService.findProviderConfigurations(pageable);
    }
  
    @ApiOperation(value = "Get a provider configuration by provider type code")
    @ApiImplicitParam(name = "providerType", value = "code of provider type", required = true, dataType = "String", paramType = "path")
    @GetMapping(value =  "/{providerType}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderConfigurationDto findProviderConfigurationByProviderType(
            @PathVariable String providerType) {
        return providerConfigurationService.findProviderConfigurationsByProviderTypeCode(providerType);
    }

    @ApiOperation(value = "Update a configuration by configuration id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "identifier of a configuration item", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider Configuration Item", required = true, dataType = "ProviderConfigurationDto", paramType = "body") })
    @PutMapping(value =  "/{id}")
    @ResponseBody
    public ProviderConfigurationDto updateConfigurationById(@PathVariable UUID id,
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.updateProviderConfiguration(id, requestDto);
    }
}