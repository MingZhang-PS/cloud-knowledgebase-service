package com.sap.fsm.knowledgebase.domain.exception.handler;

import com.sap.fsm.knowledgebase.domain.exception.ProviderTypeCodePresentException;
import com.sap.fsm.knowledgebase.domain.exception.response.ErrorResponse;
import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@Unit
class GlobalExceptionHandlerTest {
    private static GlobalExceptionHandler globalExceptionHandler;

    @BeforeAll
    static void Before() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @DisplayName("test provider type code present exception handled properly")
    @Test
    void shouldHandleProviderTypeCodePresentExceptionProperly() {
        // given
        ProviderTypeCodePresentException ex = new ProviderTypeCodePresentException("MindTouch");

        // when
        ResponseEntity<Object> resp = globalExceptionHandler.handleBusinessException(ex);

        // then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        ErrorResponse erroresp = (ErrorResponse) (resp.getBody());
        assertThat(erroresp.get("title")).isEqualTo(ex.getReason());
        assertThat(erroresp.get("detail")).isEqualTo(ex.getMessage());
        assertThat(erroresp.get("status")).isEqualTo(ex.getStatusCode().value());
    }
}