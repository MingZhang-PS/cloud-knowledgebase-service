package com.sap.fsm.knowledgebase.domain.exception;

import java.util.UUID;

public class ArticleLinkageNotExistException extends ResourceNotExistException {
    public ArticleLinkageNotExistException(UUID id) {
        super(id);
    }
}
