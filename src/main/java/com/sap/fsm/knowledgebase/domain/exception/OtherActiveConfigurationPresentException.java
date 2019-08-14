package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;

public class OtherActiveConfigurationPresentException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String reason = "Other configuration has been activated";
    private static final String errorMsg = "The is another configuration has been activated, please disable it first";

    public OtherActiveConfigurationPresentException() {
        super(errorMsg);
    }

    @Override
    public final HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public final String getReason() {
        return reason;
    }

}
