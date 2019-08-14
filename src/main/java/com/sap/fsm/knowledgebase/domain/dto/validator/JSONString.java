package com.sap.fsm.knowledgebase.domain.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = JSONStringValidator.class)
@Documented
public @interface JSONString {

    String message() default "Non-JSON string type is not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
