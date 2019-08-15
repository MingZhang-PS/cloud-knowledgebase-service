package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.GeneralSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;

import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;



@Api(value = "KnowledgeBase General Setting API", tags = { "KnowledgeBase General Setting API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/general-settings", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseGeneralSettingApi {
    @Autowired
    private GeneralSettingService generalSettingService;

    @ApiOperation(value = "Create a general setting item")
    @PostMapping
    @ResponseBody
    public GeneralSettingDto createGeneralSetting(
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.createKnowledgeGeneralSetting(requestDto);
    }
  
    @ApiOperation(value = "Get all general setting items")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "Page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<GeneralSettingDto> findGeneralSettings(@ApiIgnore(
        "Ignored because swagger ui shows the wrong params, " + 
        "instead they are explained in the implicit params") 
        Pageable pageable) {
        return generalSettingService.findGeneralSettings(pageable);
    }

    @ApiOperation(value = "Update a general setting item by key")
    @PutMapping(value = "/{key}")
    @ResponseBody
    public GeneralSettingDto updateGeneralSettingByKey(@PathVariable String key,
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.updateBySettingKey(key, requestDto);
    }

    @ApiOperation(value = "Get a general setting item by key")
    @GetMapping(value = "/{key}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public GeneralSettingDto findGeneralSettingByKey(@PathVariable String key) {
        return generalSettingService.findBySettingKey(key);
    }
}