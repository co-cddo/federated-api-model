package uk.gov.api.springboot.config;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.api.springboot.exceptions.CorrelationIdMalformedException;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String uuid = request.getHeader("correlation-id");
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
    }
    if (!uuid.matches(Patterns.UUID_STRING)) {
      throw new CorrelationIdMalformedException();
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      response.addHeader("correlation-id", uuid);
    }
  }
}
