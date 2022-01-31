package uk.gov.api.springboot.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String correlationId = request.getHeader("correlation-id");
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    try {
      filterChain.doFilter(request, response);
    } finally {
      response.addHeader("correlation-id", correlationId);
    }
  }
}
