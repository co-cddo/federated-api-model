package uk.gov.api.springboot.exceptions;

import javax.servlet.ServletException;

public class CorrelationIdMalformedException extends ServletException {

  public CorrelationIdMalformedException() {
    super("The correlation-id is not a valid UUID.");
  }
}
