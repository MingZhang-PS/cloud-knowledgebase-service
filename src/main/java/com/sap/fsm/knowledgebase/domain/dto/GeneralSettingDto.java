package com.sap.fsm.knowledgebase.domain.dto;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

import java.util.Date;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "Knowledge base general setting dto model")
@Data
public class GeneralSettingDto  {
    @ApiModelProperty(value = "Timestamp when knowledge base general setting was last changed")
    private Date lastChanged;

    @ApiModelProperty(value = "Key of knowledge base general setting", example = "enabled", required = true)
    @NotBlank
    private String key;

    @ApiModelProperty(value = "Value of knowledge base general setting", example = "\"true\"")
    private String value;
}