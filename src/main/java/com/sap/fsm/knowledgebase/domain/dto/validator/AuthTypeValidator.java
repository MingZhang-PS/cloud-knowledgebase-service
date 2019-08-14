package com.sap.fsm.knowledgebase.domain.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sap.fsm.knowledgebase.domain.model.AuthTypeEnum;

public class AuthTypeValidator implements ConstraintValidator<AuthType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        AuthTypeEnum arr[] = AuthTypeEnum.values(); 
        for(AuthTypeEnum e : arr) {
            if (e.toString() == value) {
                return true;
            }
        }
        return false;
    }
}