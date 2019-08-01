package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.annotations.*;

import java.util.UUID;

@Api(value = "KnowledgeBase Provider Type API", tags = { "KnowledgeBase Provider Type API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class KnowledgeBaseConfigurationApi {
    private static final String PROVIDER_TYPE_RESOURCE_URL_NAME = "providerType";

    @Autowired
    private KnowledgeBaseConfigurationService knowledgeBaseConfigurationService;

    @PostMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ApiOperation(value = "Create a knowledgebase provider type")
    // TODO: No authorization support
    public ResponseEntity<KnowledgeBaseProviderTypeDto> createKnowledgeBaseProviderType(
            @ApiParam(required = true) @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(knowledgeBaseConfigurationService.createKnowledgeBaseProviderType(requestDto));
    }

    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME)
    @ApiOperation(value = "Get all knowledgebase provider type")
    public ResponseEntity<Page<KnowledgeBaseProviderTypeDto>> findByKnowledgeBaseProviderTypes(Pageable pageable) {
        return ResponseEntity.ok(knowledgeBaseConfigurationService.findByKnowledgeBaseProviderTypes(pageable));
    }
    
    @GetMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ApiOperation(value = "Get a knowledgebase provider type by id")
    public ResponseEntity<KnowledgeBaseProviderTypeDto> findByKnowledgeBaseProviderTypeId(
        @ApiParam(value = "Unique identifier of a Provider Type", required = true) @PathVariable UUID id) {
        KnowledgeBaseProviderTypeDto providerType = knowledgeBaseConfigurationService.findByKnowledgeBaseProviderTypeId(id);
        if(providerType != null) {
            return ResponseEntity.ok(providerType);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping(value = PROVIDER_TYPE_RESOURCE_URL_NAME + "/{id}")
    @ApiOperation(value = "Update a knowledgebase provider type by id")
    public ResponseEntity<KnowledgeBaseProviderTypeDto> updateByKnowledgeBaseProviderTypeId(@PathVariable UUID id, 
    @RequestBody KnowledgeBaseProviderTypeDto requestDto) {
        return ResponseEntity.ok(knowledgeBaseConfigurationService.updateByKnowledgeBaseProviderTypeId(id, requestDto));

    }
}