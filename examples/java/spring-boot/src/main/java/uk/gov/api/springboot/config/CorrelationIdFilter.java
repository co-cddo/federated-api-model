package uk.gov.api.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.api.models.metadata.ErrorResponse;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  public CorrelationIdFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
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
      response.setStatus(400);
      response.setContentType("application/vnd.uk.gov.api.v1alpha+json");

      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setError(ErrorResponse.Error.INVALID_REQUEST);
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    try {
      filterChain.doFilter(request, response);
      MDC.put("correlation-id", correlationId);
    } finally {
      response.addHeader("correlation-id", correlationId);
      MDC.clear();
    }
  }
}
