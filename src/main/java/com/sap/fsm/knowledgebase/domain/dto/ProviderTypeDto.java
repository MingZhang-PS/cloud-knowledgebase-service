package com.sap.fsm.knowledgebase.domain.dto;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.util.Date;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "Knowledge base provider type dto model")
@Data
public class ProviderTypeDto  {
    @ApiModelProperty(value = "Timestamp when knowledge base provider type was last changed")
    private Date lastChanged;

    @ApiModelProperty(value = "System code of knowledge base provider type", example = "MindTouch,SAP-Native", required = true)
    @NotBlank
    private String code;

    @ApiModelProperty(value = "Display name of knowledge base provider type")
    private String name;
}