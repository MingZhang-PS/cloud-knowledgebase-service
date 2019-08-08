package com.sap.fsm.knowledgebase.domain.exception;

public class SettingPresentException extends ResourcePresentException {
    private static final long serialVersionUID = 1L;
    private static final String errorMsg = "Setting already present for key: %s, please use new key to create";

    public SettingPresentException(String key) {
        super(String.format(errorMsg, key));
    }
}
