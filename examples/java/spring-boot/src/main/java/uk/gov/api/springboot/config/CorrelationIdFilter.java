package uk.gov.api.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.jvt.contentnegotiation.ContentTypeNegotiator;
import me.jvt.uuid.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;
  private final MdcFacade mdcFacade;
  private final ContentNegotiationFacadeImpl contentNegotiationFacade;
  private final ContentTypeNegotiator contentTypeNegotiator;
  private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationIdFilter.class);

  public CorrelationIdFilter(ObjectMapper objectMapper, MdcFacade mdcFacade, ContentNegotiationFacadeImpl contentNegotiationFacade, ContentTypeNegotiator contentTypeNegotiator) {
    this.objectMapper = objectMapper;
    this.mdcFacade = mdcFacade;
    this.contentNegotiationFacade = contentNegotiationFacade;
    this.contentTypeNegotiator = contentTypeNegotiator;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String correlationId = request.getHeader("correlation-id");
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    if (!correlationId.matches(Patterns.UUID_STRING)) {
      try {
        contentNegotiationFacade.negotiate(request, contentTypeNegotiator);
      } catch (HttpMediaTypeNotAcceptableException e) {
        response.setStatus(406);
        return;
      }
//      if(MediaType.APPLICATION_PDF.equals(contentNegotiationFacade.negotiate(request, contentTypeNegotiator))) {
//        response.setStatus(406);
//        return;
//      } else {
      response.setStatus(400);
      response.setContentType("application/vnd.uk.gov.api.v1alpha+json");

      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setError(ErrorResponse.Error.INVALID_REQUEST);
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      return;
//      }
    }
    try {
      mdcFacade.put("correlation-id", correlationId);
      filterChain.doFilter(request, response);
      LOGGER.info("A request was sent with correlation-id " + correlationId);
    } finally {
      response.addHeader("correlation-id", correlationId);
      mdcFacade.clear();
    }
  }
}
