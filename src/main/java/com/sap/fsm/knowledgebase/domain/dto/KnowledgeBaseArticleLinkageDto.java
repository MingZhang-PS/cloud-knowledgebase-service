package com.sap.fsm.knowledgebase.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@ApiModel(value = "Knowledge Base article linkage dto model")
@Data
public class KnowledgeBaseArticleLinkageDto {
    @ApiModelProperty(value = "Id of Knowledge Base article linkage dto model")
    private UUID id;

    @ApiModelProperty(value = "articleId of Knowledge Base article linkage dto model", required = true)
    private String articleId;

    @ApiModelProperty(value = "providerType of Knowledge Base article linkage dto model", required = true)
    private String providerType;

    @ApiModelProperty(value = "objectType of Knowledge Base article linkage dto model", required = true)
    private String objectType;

    @ApiModelProperty(value = "objectId Knowledge Base article linkage dto model", required = true)
    private String objectId;
}
