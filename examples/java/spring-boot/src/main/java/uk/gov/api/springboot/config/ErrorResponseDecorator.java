package uk.gov.api.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.spring.ContentNegotiator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@Component
public class ErrorResponseDecorator {

  private static final MediaType V1_ALPHA =
      MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json");

  private final ContentNegotiator negotiator;
  private final ObjectMapper objectMapper;

  public ErrorResponseDecorator(ContentNegotiator negotiator, ObjectMapper objectMapper) {
    this.negotiator = negotiator;
    this.objectMapper = objectMapper;
  }

  public void decorateWithNegotiation(
      HttpServletRequest request, HttpServletResponse response, String errorDescription)
      throws IOException {
    MediaType negotiated;
    try {
      negotiated = negotiator.negotiate(request);
    } catch (HttpMediaTypeNotAcceptableException e) {
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      return;
    }

    if (negotiated.equals(V1_ALPHA)) {
      v1AlphaJson(response, errorDescription, negotiated);
    } else {
      throw new IllegalStateException(
          "The ErrorResponseDecorator could not handle the MediaType provided, `"
              + negotiated
              + "`");
    }
  }

  private void v1AlphaJson(
      HttpServletResponse response, String errorDescription, MediaType negotiated)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType(negotiated.toString());
    var error = new ErrorResponse();
    error.setError(ErrorResponse.Error.INVALID_REQUEST);
    error.setErrorDescription(errorDescription);
    response.getWriter().write(objectMapper.writeValueAsString(error));
  }
}
