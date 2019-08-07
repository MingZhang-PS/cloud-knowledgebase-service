package com.sap.fsm.knowledgebase.domain.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class AuthTypeValidator implements ConstraintValidator<AuthType, String> {

    List<String> authTypes = Arrays.asList("NoAuththentication", "Basic", "OAuth2ClientCredential");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return authTypes.contains(value);
    }
}