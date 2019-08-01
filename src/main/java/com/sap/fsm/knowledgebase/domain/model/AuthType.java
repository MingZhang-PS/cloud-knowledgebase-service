package com.sap.fsm.knowledgebase.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public enum AuthType {
    
    @ApiModelProperty(value = "No authentication")
    NOAUTHENTICATION,
    @ApiModelProperty(value = "Basic authentication")
    BASIC,
    @ApiModelProperty(value = "OAuth2 Client Credentials")
    OAUTH2CLIENTCREDENTIALS
}
