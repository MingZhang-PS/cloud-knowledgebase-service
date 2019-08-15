package com.sap.fsm.knowledgebase.domain.exception;

public class ArticleLinkageNotExistException extends ResourceNotExistException {
    private static final long serialVersionUID = 1L;
    public ArticleLinkageNotExistException(String id) {
        super(id);
    }
}
