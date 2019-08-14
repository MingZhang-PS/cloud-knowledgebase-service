package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.ProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.ProviderTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;

import io.swagger.annotations.*;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Type API", tags = { "KnowledgeBase Provider Type API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/provider-types", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseProviderTypeApi {
    
    @Autowired
    private ProviderTypeService providerTypeService;
   

    @ApiOperation(value = "Create a provider type")
    @ApiImplicitParam(name = "requestDto", value = "Provider Type Entity", required = true, dataType = "ProviderTypeDto", paramType = "body")
    @PostMapping
    @ResponseBody
    public ProviderTypeDto createProviderType(
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.createProviderType(requestDto);
    }

    @ApiOperation(value = "Get all provider types")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderTypeDto> findProviderTypes(
        Pageable pageable) {
        return providerTypeService.findProviderTypes(pageable);
    }

    @ApiOperation(value = "Get a provider type by id")
    @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path")
    @GetMapping(value =  "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderTypeDto findProviderTypeById(@PathVariable UUID id) {
        return providerTypeService.findByProviderTypeId(id);
    }

    @ApiOperation(value = "Update a provider type by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider Type Entity", required = true, dataType = "ProviderTypeDto", paramType = "body") })
    @PutMapping(value =  "/{id}")
    @ResponseBody
    public ProviderTypeDto updateProviderTypeById(@PathVariable UUID id,
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.updateByProviderTypeId(id, requestDto);
    }
}