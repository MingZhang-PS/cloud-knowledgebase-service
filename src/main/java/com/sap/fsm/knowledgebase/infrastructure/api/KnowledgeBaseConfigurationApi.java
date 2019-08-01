package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseProviderTypeDto;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.swagger.annotations.*;

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
        return new ResponseEntity<>(knowledgeBaseConfigurationService.createKnowledgeBaseProviderType(requestDto), HttpStatus.CREATED);
    }

}