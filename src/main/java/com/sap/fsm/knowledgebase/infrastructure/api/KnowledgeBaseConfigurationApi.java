package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;

import io.swagger.annotations.*;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Type API", tags = { "KnowledgeBase Provider Type API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseConfigurationApi {
    private static final String PROVIDER_TYPE_RESOURCE_URL_NAME = "provider-types";

    @Autowired
    private KnowledgeBaseConfigurationService knowledgeBaseConfigurationService;

    @ApiOperation(value = "Create a knowledgebase provider type")
    @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider Type Entity", required = true, dataType = "KnowledgeBaseProviderTypeDto", paramType = "body")
    @PostMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ResponseBody
    public KnowledgeBaseProviderTypeDto createKnowledgeBaseProviderType(
            @Validated @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return knowledgeBaseConfigurationService.createKnowledgeBaseProviderType(requestDto);
    }

    @ApiOperation(value = "Get all knowledgebase provider type")
    @ApiImplicitParam(name = "pageable", value = "Pagination", required = false, dataType = "Pageable", paramType = "query")
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseProviderTypeDto> findByKnowledgeBaseProviderTypes(Pageable pageable) {
        return knowledgeBaseConfigurationService.findKnowledgeBaseProviderTypes(pageable);
    }

    @ApiOperation(value = "Get a knowledgebase provider type by id")
    @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path")
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public KnowledgeBaseProviderTypeDto findByKnowledgeBaseProviderTypeId(@PathVariable UUID id) {
        return knowledgeBaseConfigurationService.findByKnowledgeBaseProviderTypeId(id);
    }

    @ApiOperation(value = "Update a knowledgebase provider type by id")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "Unique identifier of a Provider Type", required = true, dataType = "UUID", paramType = "path"),
        @ApiImplicitParam(name = "requestDto", value = "KnowledgeBase Provider Type Entity", required = true, dataType = "KnowledgeBaseProviderTypeDto", paramType = "body") })
    @PutMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ResponseBody
    public KnowledgeBaseProviderTypeDto updateByKnowledgeBaseProviderTypeId(
           @PathVariable UUID id,
           @Validated @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return knowledgeBaseConfigurationService.updateByKnowledgeBaseProviderTypeId(id, requestDto);
    }
}