package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

public class ProviderTypeCodePresentException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String errorMsg = "ProviderType already present for code: %s, please use new code to create";
    private static final String reason = "Provide Type exists";

    public ProviderTypeCodePresentException(String typeCode) {
        super(String.format(errorMsg, typeCode));
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
