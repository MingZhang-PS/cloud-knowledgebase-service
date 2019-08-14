package com.sap.fsm.knowledgebase.domain.exception;

public class ProviderConfigurationPresentException extends ResourcePresentException {
    private static final long serialVersionUID = 1L;
    private static final String errorMsg = "ProviderConfiguration already present for code: %s, please use new code to create";

    public ProviderConfigurationPresentException(String code) {
        super(String.format(errorMsg, code));
    }
}
