package uk.gov.api.springboot.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import me.jvt.uuid.Patterns;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private final ErrorResponseDecorator errorResponseDecorator;

  public CorrelationIdFilter(ErrorResponseDecorator errorResponseDecorator) {

    this.errorResponseDecorator = errorResponseDecorator;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String correlationId = request.getHeader("correlation-id");
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    if (!correlationId.matches(Patterns.UUID_STRING)) {
      errorResponseDecorator.decorateWithNegotiation(
          request,
          response,
          "The value provided in the `correlation-id` header was expected to be a UUID format, but was not provided in a valid format");
      return;
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      response.addHeader("correlation-id", correlationId);
    }
  }
}
