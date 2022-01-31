package uk.gov.api.springboot.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationIdFilter.class);
  private static final String CORRELATION_ID_HEADER = "correlation-id";

  private final ErrorResponseDecorator errorResponseDecorator;
  private final MdcFacade mdcFacade;

  public CorrelationIdFilter(ErrorResponseDecorator errorResponseDecorator, MdcFacade mdcFacade) {
    this.errorResponseDecorator = errorResponseDecorator;
    this.mdcFacade = mdcFacade;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String correlationId = retrieveOrGenerate(request);
    if (!correlationId.matches(Patterns.UUID_V4_STRING)) {
      LOGGER.warn(
          "The value provided in the `correlation-id` header was not provided in a valid UUID format");
      errorResponseDecorator.decorateWithNegotiation(
          request,
          response,
          "The value provided in the `correlation-id` header was expected to be a UUID format, but was not provided in a valid format");
      return;
    }
    try {
      mdcFacade.put(MdcFacade.CORRELATION_ID, correlationId);
      LOGGER.info("A request was sent with correlation-id {}", correlationId);
      filterChain.doFilter(request, response);
    } finally {
      response.addHeader(CORRELATION_ID_HEADER, correlationId);
      mdcFacade.clear();
    }
  }

  private String retrieveOrGenerate(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER))
        .orElse(UUID.randomUUID().toString());
  }
}
