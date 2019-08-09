package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

public class ArticleLinkageNotExistException extends BusinessException {
    private final String errorMsg;

    public ArticleLinkageNotExistException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getReason() {
        return this.errorMsg;
    }
}
