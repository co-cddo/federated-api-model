package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.api.models.metadata.ErrorResponse;
import uk.gov.api.springboot.exceptions.CorrelationIdMalformedException;

class GlobalExceptionHandlerTest {

  @Nested
  class HandleCorrelationIdMalformedException {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void returns400() {
      ResponseEntity<ErrorResponse> response = setUpResponse();

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsErrorCodeAsInvalidRequest() {
      ResponseEntity<ErrorResponse> response = setUpResponse();

      assertThat(response.getBody().getError()).isEqualTo(ErrorResponse.Error.INVALID_REQUEST);
    }

    @Test
    void returnsErrorDescriptionWhenInvalidRequest() {
      ResponseEntity<ErrorResponse> response = setUpResponse();

      assertThat(response.getBody().getErrorDescription())
          .isEqualTo("The correlation-id is not a valid UUID.");
    }

    private ResponseEntity<ErrorResponse> setUpResponse() {
      return handler.handleCorrelationIdMalformedException(
          new CorrelationIdMalformedException(), null);
    }
  }
}
