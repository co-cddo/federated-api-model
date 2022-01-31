package uk.gov.api.springboot.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private static final String CORRELATION_ID_HEADER = "correlation-id";

  private final ErrorResponseDecorator errorResponseDecorator;

  public CorrelationIdFilter(ErrorResponseDecorator errorResponseDecorator) {
    this.errorResponseDecorator = errorResponseDecorator;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String correlationId = retrieveOrGenerate(request);
    if (!correlationId.matches(Patterns.UUID_V4_STRING)) {
      errorResponseDecorator.decorateWithNegotiation(
          request,
          response,
          "The value provided in the `correlation-id` header was expected to be a UUID format, but was not provided in a valid format");
      return;
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      response.addHeader(CORRELATION_ID_HEADER, correlationId);
    }
  }

  private String retrieveOrGenerate(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER))
        .orElse(UUID.randomUUID().toString());
  }
}
