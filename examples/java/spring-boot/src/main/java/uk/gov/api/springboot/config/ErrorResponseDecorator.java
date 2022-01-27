package uk.gov.api.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.contentnegotiation.ContentTypeNegotiator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@Component
public class ErrorResponseDecorator {

  private final ContentNegotiationFacade facade;
  private final ContentTypeNegotiator negotiator;
  private final ObjectMapper objectMapper;

  public ErrorResponseDecorator(
      ContentNegotiationFacade facade,
      ContentTypeNegotiator negotiator,
      ObjectMapper objectMapper) {
    this.facade = facade;
    this.negotiator = negotiator;
    this.objectMapper = objectMapper;
  }

  /**
   * Decorate the provided {@link HttpServletResponse} with an error response, as deemed by
   * performing content-negotiation based on the clients' requested representations.
   *
   * @param request the {@link HttpServletRequest} which provides information of requested client
   *     {@link MediaType}s
   * @param response the {@link HttpServletResponse} to decorate
   * @param errorDescription the error description that will be returned to the caller, if
   *     appropriate
   */
  public void decorateWithNegotiation(
      HttpServletRequest request, HttpServletResponse response, String errorDescription)
      throws IOException {
    MediaType negotiated;
    try {
      negotiated = facade.negotiate(request, negotiator);
    } catch (HttpMediaTypeNotAcceptableException e) {
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      return;
    }

    if (negotiated.isCompatibleWith(MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json"))) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType(negotiated.toString());
      ErrorResponse error = new ErrorResponse();
      error.setError(ErrorResponse.Error.INVALID_REQUEST);
      error.setErrorDescription(errorDescription);
      response.getWriter().write(objectMapper.writeValueAsString(error));
    } else {
      throw new IllegalStateException(
          "The ErrorResponseDecorator could not handle the MediaType provided, `"
              + negotiated
              + "`");
    }
  }
}
