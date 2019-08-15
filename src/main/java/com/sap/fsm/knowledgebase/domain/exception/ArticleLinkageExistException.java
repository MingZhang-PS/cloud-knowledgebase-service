package com.sap.fsm.knowledgebase.domain.exception;

public class ArticleLinkageExistException extends ResourcePresentException {
    private static final long serialVersionUID = 1L;
    public ArticleLinkageExistException(String errMsg) {
        super(errMsg);
    }
}
