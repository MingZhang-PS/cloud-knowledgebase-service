package com.sap.fsm.knowledgebase.domain.dto.validator;

import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Unit
class JSONStringValidatorTest {

	private JSONStringValidator validator = new JSONStringValidator();

	private static Stream<Arguments> validJSONStrings() {
		return Stream.of(
				Arguments.of("{\"key\": \"xyz\", \"user\": \"admin\", \"secret\": \"abc\"}")
		);
	}

	private static Stream<Arguments> invalidJSONStrings() {
		return Stream.of(
				Arguments.of("{ key: \"value }")
		);
	}

	@DisplayName("test correct json strings")
	@ParameterizedTest
	@MethodSource("validJSONStrings")
	void valuesShouldBeValid(String json) {
		assertThat(validator.isValid(json, null))
				.isEqualTo(true);
	}

	@DisplayName("test wrong json strings")
	@ParameterizedTest
	@MethodSource("invalidJSONStrings")
	void valuesShouldBeInvalid(String json) {
		assertThat(validator.isValid(json, null))
				.isEqualTo(false);
	}
}