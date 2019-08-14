package com.sap.fsm.knowledgebase.domain.exception;

public class ArticleLinkageNotExistException extends ResourceNotExistException {
    public ArticleLinkageNotExistException(String id) {
        super(id);
    }
}
