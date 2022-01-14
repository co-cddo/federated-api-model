package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.exceptions.CorrelationIdMalformedException;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;

  private final CorrelationIdFilter filter = new CorrelationIdFilter();

  @Test
  void delegatesToFilterChain() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void validCorrelationIdIsAddedToResponse() throws ServletException, IOException {
    String uuid = UUID.randomUUID().toString();
    when(request.getHeader("correlation-id")).thenReturn(uuid);
    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", uuid);
  }

  @Test
  void correlationIdIsAddedToResponseRegardlessOfFilterErrors()
      throws ServletException, IOException {
    String uuid = UUID.randomUUID().toString();
    when(request.getHeader("correlation-id")).thenReturn(uuid);
    doThrow(new IOException()).when(filterChain).doFilter(any(), any());
    assertThatThrownBy(() -> filter.doFilterInternal(request, response, filterChain))
        .isInstanceOf(IOException.class);

    // TODO: add correlation-id to the response in the OpenAPI spec
    verify(response).addHeader("correlation-id", uuid);
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"invalid"})
  void throwAnExceptionIfUuidIsInvalid(String correlationId) {
    when(request.getHeader("correlation-id")).thenReturn(correlationId);

    assertThatThrownBy(() -> filter.doFilterInternal(request, response, filterChain))
        .isInstanceOf(CorrelationIdMalformedException.class);
  }

  @Test
  void uuidIsGeneratedIfNoneProvided() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader(eq("correlation-id"), matches(Patterns.UUID_STRING)); // TODO
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "93094CAB-21D7-13EC-97E9-566573544781",
        "93094CAB-21D7-43EC-97E9-566573544781",
        "93094cab-21d7-43ec-97e9-566573544781"
      })
  void anyVersionOfUuidIsAccepted(String uuid) throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn(uuid);
    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", uuid);
  }
}
