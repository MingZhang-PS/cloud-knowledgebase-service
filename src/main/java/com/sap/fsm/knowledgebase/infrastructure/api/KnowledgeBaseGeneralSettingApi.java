package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.GeneralSettingDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.GeneralSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import io.swagger.annotations.*;


@Api(value = "KnowledgeBase General Setting API", tags = { "KnowledgeBase General Setting API" })
@RestController
@RequestMapping(path = "/api/knowledge-base/v1/general-settings", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KnowledgeBaseGeneralSettingApi {

    @Autowired
    private GeneralSettingService generalSettingService;


    @ApiOperation(value = "Create a general setting item")
    @ApiImplicitParam(name = "requestDto", value = "General Setting Item", required = true, dataType = "GeneralSettingDto", paramType = "body")
    @PostMapping
    @ResponseBody
    public GeneralSettingDto createGeneralSetting(
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.createKnowledgeGeneralSetting(requestDto);
    }
  
    @ApiOperation(value = "Get all general setting items")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page offset", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "page size", required = false, dataType = "int", paramType = "query") })
    @GetMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public PaginationRecord<GeneralSettingDto> findGeneralSettings(
       Pageable pageable) {
        return generalSettingService.findGeneralSettings(pageable);
    }

    @ApiOperation(value = "Update a general setting item by key")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "Key of a geneal setting item", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "requestDto", value = "Provider General Setting Item", required = true, dataType = "GeneralSettingDto", paramType = "body") })
    @PutMapping(value = "/{key}")
    @ResponseBody
    public GeneralSettingDto updateGeneralSettingById(@PathVariable String key,
            @Validated @RequestBody GeneralSettingDto requestDto) {
        return generalSettingService.updateBySettingKey(key, requestDto);
    }

    @ApiOperation(value = "Get a general setting by key")
    @ApiImplicitParam(name = "key", value = "Key of a general setting item", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{key}", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public GeneralSettingDto findGeneralSettingById(@PathVariable String key) {
        return generalSettingService.findBySettingKey(key);
    }
}