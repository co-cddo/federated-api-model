package uk.gov.api.springboot.infrastructure.config;

import static com.github.valfirst.slf4jtest.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

  private static final String EXAMPLE_VALID_CORRELATION_ID = "93094CAB-21D7-43EC-97E9-566573544781";
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @Mock private ErrorResponseDecorator errorResponseDecorator;
  @Mock private MdcFacade mdcFacade;
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
      strings = {"93094CAB-21D7-43EC-97E9-566573544781", "93094cab-21d7-43ec-97e9-566573544781"})
  void uuidVersionFourIsAccepted(String correlationId) throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn(correlationId);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).addHeader("correlation-id", correlationId);
  }

  @Test
  void uuidVersionOneIsNotAccepted() throws ServletException, IOException {
    when(request.getHeader("correlation-id")).thenReturn("93094CAB-21D7-13EC-97E9-566573544781");

    filter.doFilterInternal(request, response, filterChain);

    verify(errorResponseDecorator)
        .decorateWithNegotiation(
            request,
            response,
            "The value provided in the `correlation-id` header was expected to be a UUID format, but was not provided in a valid format");

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
        when(request.getHeader("correlation-id")).thenReturn(EXAMPLE_VALID_CORRELATION_ID);

        filter.doFilterInternal(request, response, filterChain);

        InOrder inOrder = inOrder(mdcFacade, filterChain);
        inOrder.verify(mdcFacade).put("correlation-id", EXAMPLE_VALID_CORRELATION_ID);
        inOrder.verify(filterChain).doFilter(request, response);
      }

      @Test
      void thatARequestWasSent() throws ServletException, IOException {
        when(request.getHeader("correlation-id")).thenReturn(EXAMPLE_VALID_CORRELATION_ID);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.info(
                    "A request was sent with correlation-id {}", EXAMPLE_VALID_CORRELATION_ID));
      }

      @Test
      void messageIsLoggedBeforeFilterChainContinues() throws ServletException, IOException {
        when(request.getHeader("correlation-id")).thenReturn(EXAMPLE_VALID_CORRELATION_ID);
        doThrow(new IOException()).when(filterChain).doFilter(any(), any());

        assertThatThrownBy(() -> filter.doFilterInternal(request, response, filterChain))
            .isInstanceOf(IOException.class);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.info(
                    "A request was sent with correlation-id {}", EXAMPLE_VALID_CORRELATION_ID));
      }

      @Test
      void thatThereWasAnErrorResponse() throws ServletException, IOException {
        when(request.getHeader("correlation-id")).thenReturn("invalid");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.warn(
                    "The value provided in the `correlation-id` header was not provided in a valid UUID format"));
      }

      @Test
      void mdcIsClearedAfterAddingHeaders() throws ServletException, IOException {
        filter.doFilterInternal(request, response, filterChain);

        InOrder inOrder = inOrder(response, mdcFacade);
        inOrder.verify(response).addHeader(any(), any());
        inOrder.verify(mdcFacade).clear();
      }
    }
  }
}
