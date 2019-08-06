package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProviderTypeNotExistException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String errorMsg = "ProviderType not found for id: %s";
    private static final String reason = "ProviderType not found";

    public ProviderTypeNotExistException(UUID id) {
        super(String.format(errorMsg, id.toString()));
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getReason() {
        return reason;
    }

}
