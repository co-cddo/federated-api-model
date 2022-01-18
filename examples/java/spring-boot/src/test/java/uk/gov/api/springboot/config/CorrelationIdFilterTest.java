package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.uuid.Patterns;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @Mock private ObjectMapper mapper;
  @InjectMocks private CorrelationIdFilter filter;

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

  @Nested
  class UuidIsInvalid {

    @Mock private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
      when(response.getWriter()).thenReturn(writer);
    }

    private void runFilter(String correlationId) throws ServletException, IOException {
      when(request.getHeader("correlation-id")).thenReturn(correlationId);
      filter.doFilterInternal(request, response, filterChain);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"invalid"})
    void aBadRequestIsReturnedIfUuidIsInvalid(String correlationId)
        throws ServletException, IOException {
      runFilter(correlationId);

      verify(response).setStatus(400);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"invalid"})
    void contentTypeIsV1AlphaJsonIfUuidIsInvalid(String correlationId)
        throws ServletException, IOException {
      runFilter(correlationId);

      verify(response).setContentType("application/vnd.uk.gov.api.v1alpha+json");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"invalid"})
    void responseBodyIsAddedIfUuidIsInvalid(String correlationId)
        throws ServletException, IOException {
      when(mapper.writeValueAsString(any())).thenReturn("Serialised JSON");
      when(response.getWriter()).thenReturn(writer);
      runFilter(correlationId);

      verify(writer).write("Serialised JSON");
    }
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"invalid"})
  void responseBodyIsAddedIfUuidIsInvalid(
      String correlationId, @Mock PrintWriter writer, @Mock ObjectMapper mapper)
      throws ServletException, IOException {
    CorrelationIdFilter filter = new CorrelationIdFilter(mapper);
    when(request.getHeader("correlation-id")).thenReturn(correlationId);
    when(mapper.writeValueAsString(any())).thenReturn("Serialised JSON");
    when(response.getWriter()).thenReturn(writer);

    filter.doFilterInternal(request, response, filterChain);

    verify(writer).write("Serialised JSON");
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
