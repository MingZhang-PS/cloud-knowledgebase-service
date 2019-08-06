package com.sap.fsm.knowledgebase.domain.exception.handler;

import com.sap.fsm.knowledgebase.domain.exception.BusinessException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypeCodePresentException;
import com.sap.fsm.knowledgebase.domain.exception.ProviderTypeNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.response.ErrorResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResp = new ErrorResponse();
        StringBuilder resultBuilder = new StringBuilder();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (errors != null && errors.size() > 0) {
            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    String fieldName = fieldError.getField();
                    String fieldErrMsg = fieldError.getDefaultMessage();
                    resultBuilder.append(fieldName).append(" ").append(fieldErrMsg).append(";");
                }
            }
        }
        errorResp.setDetail(resultBuilder.toString());
        errorResp.setStatus(status);
        return new ResponseEntity<>(errorResp, status);
    }

    @ExceptionHandler({ProviderTypeCodePresentException.class, ProviderTypeNotExistException.class})
    public final ResponseEntity<Object> handleProviderTypeCodePresentException(BusinessException ex) {
        ErrorResponse errorResp = new ErrorResponse();
        errorResp.setDetail(ex.getMessage());
        errorResp.setStatus(ex.getStatusCode());
        errorResp.setTitle(ex.getReason());
        return new ResponseEntity<>(errorResp, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception ex) {
        ErrorResponse errorResp = new ErrorResponse();
        errorResp.setDetail(ex.getMessage());
        return new ResponseEntity<>(errorResp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}