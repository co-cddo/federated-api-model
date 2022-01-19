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

  private ObjectMapper mapper;

  public CorrelationIdFilter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String uuid = request.getHeader("correlation-id");
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
    }
    if (!uuid.matches(Patterns.UUID_STRING)) {
      response.setStatus(400);
      response.setContentType("application/vnd.uk.gov.api.v1alpha+json");

      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setError(ErrorResponse.Error.INVALID_REQUEST);
      response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
    try {
      filterChain.doFilter(request, response);
      MDC.put("correlation-id", uuid);
    } finally {
      response.addHeader("correlation-id", uuid);
      MDC.clear();
    }
  }
}
