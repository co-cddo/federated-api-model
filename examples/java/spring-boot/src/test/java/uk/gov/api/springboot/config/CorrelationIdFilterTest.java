package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @Mock private ObjectMapper mapper;
  @Mock private MdcFacade mdcFacade;
  @InjectMocks private CorrelationIdFilter filter;

  @Test
  void delegatesToFilterChain() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void validCorrelationIdIsAddedToResponse() throws ServletException, IOException {
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

    // TODO: add correlation-id to the response in the OpenAPI spec
    verify(response).addHeader("correlation-id", correlationId);
  }

  @Nested
  class UuidIsInvalid {

    @Mock private PrintWriter writer;
    @Captor private ArgumentCaptor<ErrorResponse> errorResponseArgumentCaptor;

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
    void aBadRequestIsReturned(String correlationId) throws ServletException, IOException {
      runFilter(correlationId);

      verify(response).setStatus(400);
    }

    @Test
    void contentTypeIsV1AlphaJson() throws ServletException, IOException {
      String correlationId = "invalid";
      runFilter(correlationId);

      verify(response).setContentType("application/vnd.uk.gov.api.v1alpha+json");
    }

    @Test
    void responseBodyIsAdded() throws ServletException, IOException {
      String correlationId = "invalid";
      when(mapper.writeValueAsString(any())).thenReturn("Serialised JSON");
      when(response.getWriter()).thenReturn(writer);
      runFilter(correlationId);

      verify(writer).write("Serialised JSON");
    }

    @Test
    void errorResponseIsReturned() throws IOException, ServletException {
      ErrorResponse expected = new ErrorResponse();
      expected.setError(ErrorResponse.Error.INVALID_REQUEST);

      runFilter("");
      verify(mapper).writeValueAsString(errorResponseArgumentCaptor.capture());

      assertThat(errorResponseArgumentCaptor.getValue())
          .usingRecursiveComparison()
          .isEqualTo(expected);
    }
  }

  @Nested
  class Log {

    private final TestLogger logger = TestLoggerFactory.getTestLogger(CorrelationIdFilter.class);

    @AfterEach
    void tearDown() {
      logger.clear();
    }

    @Test
    void correlationIdIsAddedToMdcBeforeFilterChainContinues()
        throws ServletException, IOException {
      String correlationId = "93094CAB-21D7-13EC-97E9-566573544781";
      when(request.getHeader("correlation-id")).thenReturn(correlationId);

      filter.doFilterInternal(request, response, filterChain);

      InOrder inOrder = inOrder(mdcFacade, filterChain);
      inOrder.verify(mdcFacade).put("correlation-id", correlationId);
      inOrder.verify(filterChain).doFilter(request, response);
    }
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
        "93094CAB-21D7-13EC-97E9-566573544781",
        "93094CAB-21D7-43EC-97E9-566573544781",
        "93094cab-21d7-43ec-97e9-566573544781"
      })
  void anyVersionOfUuidIsAccepted(String correlationId) throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn(correlationId);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", correlationId);
  }
}
