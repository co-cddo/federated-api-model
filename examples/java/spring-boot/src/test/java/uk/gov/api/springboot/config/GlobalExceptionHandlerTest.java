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

    @Test
    void returns400() {
      var handler = new GlobalExceptionHandler();
      ResponseEntity<ErrorResponse> response =
          handler.handleCorrelationIdMalformedException(
              new CorrelationIdMalformedException(), null);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsErrorCodeAsInvalidRequest() {
      var handler = new GlobalExceptionHandler();
      ResponseEntity<ErrorResponse> response =
          handler.handleCorrelationIdMalformedException(
              new CorrelationIdMalformedException(), null);

      assertThat(response.getBody().getError()).isEqualTo(ErrorResponse.Error.INVALID_REQUEST);
    }

    @Test
    void returnsErrorDescriptionWhenInvalidRequest() {
      var handler = new GlobalExceptionHandler();
      ResponseEntity<ErrorResponse> response =
          handler.handleCorrelationIdMalformedException(
              new CorrelationIdMalformedException(), null);

      assertThat(response.getBody().getErrorDescription())
          .isEqualTo("The correlation-id is not a valid UUID.");
    }
  }
}
