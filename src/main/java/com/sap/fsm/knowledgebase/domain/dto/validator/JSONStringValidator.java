package com.sap.fsm.knowledgebase.domain.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.gson.JsonParser;

public class JSONStringValidator implements ConstraintValidator<JSONString, String> {
    private static final JsonParser parser = new JsonParser();
    @Override
    public boolean isValid(String json, ConstraintValidatorContext context) {
        if (json == null) return true;
        try {
            parser.parse(json);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) { 
            return false;
        }
    }
}