package uk.gov.api.springboot.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uk.gov.api.models.metadata.ErrorResponse;
import uk.gov.api.springboot.exceptions.CorrelationIdMalformedException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CorrelationIdMalformedException.class)
  protected ResponseEntity<ErrorResponse> handleCorrelationIdMalformedException(
      CorrelationIdMalformedException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError(ErrorResponse.Error.INVALID_REQUEST);
    errorResponse.setErrorDescription(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
