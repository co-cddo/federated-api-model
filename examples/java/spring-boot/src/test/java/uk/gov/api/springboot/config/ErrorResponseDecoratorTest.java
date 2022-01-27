package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jvt.contentnegotiation.ContentTypeNegotiator;
import me.jvt.contentnegotiation.NotAcceptableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class ErrorResponseDecoratorTest {
  @Mock private ContentNegotiationFacade contentNegotiationFacade;
  @Mock private ContentTypeNegotiator negotiator;
  @Mock private ObjectMapper mapper;

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;

  @Mock private PrintWriter writer;

  @InjectMocks private ErrorResponseDecorator decorator;

  @Nested
  class DecorateWithNegotiation {

    @Nested
    class WhenNegotiationFails {

      @BeforeEach
      void setup() throws HttpMediaTypeNotAcceptableException, IOException {
        when(contentNegotiationFacade.negotiate(any(), any()))
            .thenThrow(new HttpMediaTypeNotAcceptableException(""));

        decorator.decorateWithNegotiation(request, response);
      }

      @Test
      void delegatesRequestToFacade()
          throws HttpMediaTypeNotAcceptableException, IOException, NotAcceptableException {
        verify(contentNegotiationFacade).negotiate(eq(request), any());
      }

      @Test
      void delegatesNegotiationToFacade() throws HttpMediaTypeNotAcceptableException, IOException {
        verify(contentNegotiationFacade).negotiate(any(), eq(negotiator));
      }

      @Test
      void returnsNotAcceptable() {
        verify(response).setStatus(406);
      }

      @Test
      void doesNotSendABody() {
        verifyNoInteractions(writer);
      }

      @Test
      void doesNotSetContentType() {
        verify(response, times(0)).setContentType(any());
      }
    }

    @Nested
    class WhenNegotiationSucceeds {

      @Nested
      class V1Alpha {

        @Captor private ArgumentCaptor<ErrorResponse> errorResponseArgumentCaptor;
        @Mock private MediaType mediaType;

        @BeforeEach
        void setUp() throws IOException, HttpMediaTypeNotAcceptableException {
          when(response.getWriter()).thenReturn(writer);
          when(mapper.writeValueAsString(any())).thenReturn("Serialised JSON");

          when(contentNegotiationFacade.negotiate(any(), any())).thenReturn(mediaType);
          when(mediaType.toString()).thenReturn("v1-version");

          when(mediaType.isCompatibleWith(
                  MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json")))
              .thenReturn(true);

          decorator.decorateWithNegotiation(request, response);
        }

        @Test
        void aBadRequestIsReturned() {
          verify(response).setStatus(400);
        }

        @Test
        void contentTypeIsV1AlphaJson() throws ServletException, IOException {
          verify(response).setContentType("v1-version");
        }

        @Test
        void responseBodyIsAdded() throws ServletException, IOException {
          verify(writer).write("Serialised JSON");
        }

        @Test
        void errorResponseIsReturned() throws IOException, ServletException {
          ErrorResponse expected = new ErrorResponse();
          expected.setError(ErrorResponse.Error.INVALID_REQUEST);

          verify(mapper).writeValueAsString(errorResponseArgumentCaptor.capture());

          assertThat(errorResponseArgumentCaptor.getValue())
              .usingRecursiveComparison()
              .isEqualTo(expected);
        }
      }

      @Nested
      class Unmatched {
        @Test
        void throwsIllegalStateException(@Mock MediaType mediaType)
            throws HttpMediaTypeNotAcceptableException {
          when(contentNegotiationFacade.negotiate(any(), any())).thenReturn(mediaType);
          when(mediaType.toString()).thenReturn("in/valid");
          when(mediaType.isCompatibleWith(
                  MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json")))
              .thenReturn(false);

          assertThatThrownBy(() -> decorator.decorateWithNegotiation(request, response))
              .isInstanceOf(IllegalStateException.class)
              .hasMessage(
                  "The ErrorResponseDecorator could not handle the MediaType provided, `in/valid`");
        }
      }
    }
  }
}
