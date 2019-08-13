package com.sap.fsm.knowledgebase.domain.dto.validator;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JSONStringValidator implements ConstraintValidator<JSONString, String> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public boolean isValid(String json, ConstraintValidatorContext context) {
        if(json == null) {
            return true;
        }
        try {
            mapper.readTree(json);
            return true;
         } catch (IOException e) {
            return false;
         }
    }
}