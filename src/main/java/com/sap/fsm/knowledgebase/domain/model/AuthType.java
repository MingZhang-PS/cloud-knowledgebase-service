package com.sap.fsm.knowledgebase.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public enum AuthType {
    
    @ApiModelProperty(value = "No authentication")
    NoAuththentication,
    @ApiModelProperty(value = "Basic authentication")
    Basic,
    @ApiModelProperty(value = "OAuth2 Client Credentials")
    OAuth2ClientCredential
}
