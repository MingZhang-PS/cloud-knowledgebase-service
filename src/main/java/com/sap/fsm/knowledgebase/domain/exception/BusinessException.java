package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public abstract HttpStatus getStatusCode();

    public abstract String getReason();

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}