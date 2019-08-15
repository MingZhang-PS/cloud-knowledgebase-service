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
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Configuration API", tags = { "KnowledgeBase Provider Configuration API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/provider-configurations", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseProviderConfigurationApi {
    @Autowired
    private ProviderConfigurationService providerConfigurationService;

    @ApiOperation(value = "Create a provider configuration")
    @PostMapping
    @ResponseBody
    public ProviderConfigurationDto createProviderConfiguration(
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.createProviderConfiguration(requestDto);
    }

    @ApiOperation(value = "Get all provider configuration")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "Page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping( consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderConfigurationDto> findProviderConfiguration(@ApiIgnore(
        "Ignored because swagger ui shows the wrong params, " +
        "instead they are explained in the implicit params") 
        Pageable pageable) {
        return providerConfigurationService.findProviderConfigurations(pageable);
    }
  
    @ApiOperation(value = "Get a provider configuration by provider type code")
    @GetMapping(value =  "/{provider-type}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderConfigurationDto findProviderConfigurationByProviderType(
            @PathVariable(name="provider-type") String providerType) {
        return providerConfigurationService.findProviderConfigurationsByProviderTypeCode(providerType);
    }

    @ApiOperation(value = "Update a configuration by configuration id")
    @PutMapping(value =  "/{id}")
    @ResponseBody
    public ProviderConfigurationDto updateConfigurationById(@PathVariable UUID id,
            @Validated @RequestBody ProviderConfigurationDto requestDto) {
        return providerConfigurationService.updateProviderConfiguration(id, requestDto);
    }
}