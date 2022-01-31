package uk.gov.api.springboot.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jvt.contentnegotiation.ContentTypeNegotiator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import me.jvt.spring.ContentNegotiator;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorResponseDecoratorTest {
  @Mock private ContentNegotiator contentNegotiator;
  @Mock private ObjectMapper mapper;

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;

  @Mock private PrintWriter writer;

  @InjectMocks
  private ErrorResponseDecorator decorator;

  @Nested
  class DecorateWithNegotiation {

    @Nested
    class WhenNegotiationFails {
      @BeforeEach
      void setup() throws HttpMediaTypeNotAcceptableException, IOException {
        when(contentNegotiator.negotiate((HttpServletRequest) any()))
            .thenThrow(new HttpMediaTypeNotAcceptableException(""));

        decorator.decorateWithNegotiation(request, response, "error description");
      }

      @Test
      void delegatesRequestToNegotiator() throws HttpMediaTypeNotAcceptableException {
        verify(contentNegotiator).negotiate(eq(request));
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

          when(contentNegotiator.negotiate((HttpServletRequest) any())).thenReturn(mediaType);
          when(mediaType.toString()).thenReturn("v1-version");

          when(mediaType.isCompatibleWith(
                  MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json")))
                  .thenReturn(true);

          decorator.decorateWithNegotiation(request, response, "error description");
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

      }
    }

    @Nested
    class Unmatched {
      @Test
      void throwsIllegalStateException(@Mock MediaType mediaType)
              throws HttpMediaTypeNotAcceptableException {
        when(contentNegotiator.negotiate((HttpServletRequest) any())).thenReturn(mediaType);
        when(mediaType.toString()).thenReturn("in/valid");
        when(mediaType.isCompatibleWith(
                MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json")))
                .thenReturn(false);

        assertThatThrownBy(
                () -> decorator.decorateWithNegotiation(request, response, "error description"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "The ErrorResponseDecorator could not handle the MediaType provided, `in/valid`");
      }
    }

  }
}
