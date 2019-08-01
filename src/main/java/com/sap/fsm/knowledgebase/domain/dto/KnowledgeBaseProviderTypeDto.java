package com.sap.fsm.knowledgebase.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.UUID;

@ApiModel(value = "Knowledge base provider type dto model")
@Data
public class KnowledgeBaseProviderTypeDto {
    @ApiModelProperty(value = "Unique identifier of the knowledge base provider type")
    private UUID id;

    @ApiModelProperty(value = "Timestamp when knowledge base provider type was last changed", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastChanged;

    @ApiModelProperty(value = "System code of knowledge base provider type", example = "MindTouch,SAP-Native", required = true)
    private String code;

    @ApiModelProperty(value = "Display name of knowledge base provider type")
    private String name;
}