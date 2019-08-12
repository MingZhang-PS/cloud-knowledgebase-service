package com.sap.fsm.knowledgebase.domain.exception;

public class ProviderTypePresentException extends ResourcePresentException {
    private static final long serialVersionUID = 1L;
    private static final String errorMsg = "ProviderType already present for code: %s, please use new code to create";

    public ProviderTypePresentException(String code) {
        super(String.format(errorMsg, code));
    }
}
