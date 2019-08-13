package com.sap.fsm.knowledgebase.domain.dto;

import com.sap.fsm.knowledgebase.domain.dto.validator.AuthType;
import com.sap.fsm.knowledgebase.domain.dto.validator.JSONString;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

import java.util.Date;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Knowledge base provider configuration dto model")
@Data
public class KnowledgeBaseProviderConfigurationDto  {
    @ApiModelProperty(value = "Unique identifier of the knowledge base provider configuration")
    private UUID id;

    @ApiModelProperty(value = "Timestamp when knowledge base provider configuration was last changed")
    private Date lastChanged;

    @ApiModelProperty(value = "Unique identifier of knowledge base provider type", required = true)
    @NotNull
    private UUID providerType;

    @ApiModelProperty(value = "If the knowledge base provider configuration is activated", required = true)
    @NotNull
    private Boolean isActive;

    @ApiModelProperty(value = "The authentication type of knowledge base adapter", example = "Basic,OAuth2ClientCredential")
    @AuthType
    private String adapterAuthType;

    @ApiModelProperty(value = "The URL of knowledge base adapter")
    @URL
    private String adapterURL;

    @ApiModelProperty(value = "The credential of knowledge base adapter")
    @JSONString
    private String adapterCredential;

    @ApiModelProperty(value = "The authentication type of knowledge base provider site")
    private String siteAuthType;

    @ApiModelProperty(value = "The URL of knowledge base provider site")
    @URL
    private String siteURL;

    @ApiModelProperty(value = "The credential of knowledge base provider site")
    @JSONString
    private String siteCredential;
}