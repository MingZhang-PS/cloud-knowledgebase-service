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
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "KnowledgeBase Provider Type API", tags = { "KnowledgeBase Provider Type API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/provider-types", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseProviderTypeApi {
    
    @Autowired
    private ProviderTypeService providerTypeService;
   
    @ApiOperation(value = "Create a provider type")
    @PostMapping
    @ResponseBody
    public ProviderTypeDto createProviderType(
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.createProviderType(requestDto);
    }

    @ApiOperation(value = "Get all provider types")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "Page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<ProviderTypeDto> findProviderTypes(@ApiIgnore(
        "Ignored because swagger ui shows the wrong params, " + 
        "instead they are explained in the implicit params")
        Pageable pageable) {
        return providerTypeService.findProviderTypes(pageable);
    }

    @ApiOperation(value = "Get a provider type by code")
    @GetMapping(value =  "/{code}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ProviderTypeDto findProviderTypeByCode(@PathVariable String code) {
        return providerTypeService.findByProviderTypeCode(code);
    }

    @ApiOperation(value = "Update a provider type by code")
    @PutMapping(value =  "/{code}")
    @ResponseBody
    public ProviderTypeDto updateProviderTypeById(@PathVariable String code,
            @Validated @RequestBody ProviderTypeDto requestDto) {
        return providerTypeService.updateByProviderTypeCode(code, requestDto);
    }
}