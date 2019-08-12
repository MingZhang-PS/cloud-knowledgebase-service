package com.sap.fsm.knowledgebase.domain.exception;

import org.springframework.http.HttpStatus;
import java.util.UUID;


public class ResourceNotExistException extends BusinessException {
    private static final long serialVersionUID = 1L;
    private static final String reason = "Resource not found";
    private static final String errorMsg = "Resource not found for id: %s";

    public ResourceNotExistException(UUID id) {
        super(String.format(errorMsg, id.toString()));
    }

    @Override
    public final HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public final String getReason() {
        return reason;
    }

}
