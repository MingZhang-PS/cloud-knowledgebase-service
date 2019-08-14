package com.sap.fsm.knowledgebase.domain.dto.validator;

import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeAll;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Unit
class AuthTypeValidatorTest {

	private static AuthTypeValidator validator;

	@BeforeAll
    static void Before() {
        validator = new AuthTypeValidator();
    }

	private static Stream<Arguments> validAuthTypes() {
		return Stream.of(
				Arguments.of("NoAuthentication"),
				Arguments.of("OAuth2ClientCredential")
		);
	}

	private static Stream<Arguments> invalidAuthTypes() {
		return Stream.of(
				Arguments.of(""),
				Arguments.of("SAML")
		);
	}

	@DisplayName("test correct auth types")
	@ParameterizedTest
	@MethodSource("validAuthTypes")
	void valuesShouldBeValid(String authType) {
		assertThat(validator.isValid(authType, null))
				.isEqualTo(true);
	}

	@DisplayName("test wrong auth types")
	@ParameterizedTest
	@MethodSource("invalidAuthTypes")
	void valuesShouldBeInvalid(String authType) {
		assertThat(validator.isValid(authType, null))
				.isEqualTo(false);
	}
}