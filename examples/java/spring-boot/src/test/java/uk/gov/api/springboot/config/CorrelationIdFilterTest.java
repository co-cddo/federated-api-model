package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @Mock private ErrorResponseDecorator errorResponseDecorator;
  @InjectMocks private CorrelationIdFilter filter;

  @Test
  void delegatesToFilterChain() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void correlationIdIsAddedToResponse() throws ServletException, IOException {
    String correlationId = UUID.randomUUID().toString();
    when(request.getHeader("correlation-id")).thenReturn(correlationId);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", correlationId);
  }

  @Test
  void correlationIdIsAddedToResponseRegardlessOfFilterErrors()
      throws ServletException, IOException {
    String correlationId = UUID.randomUUID().toString();
    when(request.getHeader("correlation-id")).thenReturn(correlationId);
    doThrow(new IOException()).when(filterChain).doFilter(any(), any());

    assertThatThrownBy(() -> filter.doFilterInternal(request, response, filterChain))
        .isInstanceOf(IOException.class);

    verify(response).addHeader("correlation-id", correlationId);
  }

  @Test
  void uuidIsGeneratedIfNoneProvided() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(response)
        .addHeader(
            eq("correlation-id"),
            matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"));
  }

  @ParameterizedTest
  @ValueSource(
          strings = {
                  "93094CAB-21D7-43EC-97E9-566573544781",
                  "93094cab-21d7-43ec-97e9-566573544781"
          })
  void uuidVersionFourIsAccepted(String correlationId) throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn(correlationId);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", correlationId);
  }

  @Test
  void uuidVersionOneIsNotAccepted() throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn("93094CAB-21D7-13EC-97E9-566573544781");

    filter.doFilterInternal(request, response, filterChain);

    verify(errorResponseDecorator).decorateWithNegotiation(request, response, "The value provided in the `correlation-id` header was expected to be a UUID format, but was not provided in a valid format");
  }
}
