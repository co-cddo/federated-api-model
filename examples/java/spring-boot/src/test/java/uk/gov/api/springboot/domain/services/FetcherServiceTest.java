package uk.gov.api.springboot.domain.services;

import static com.github.valfirst.slf4jtest.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.Registry;
import uk.gov.api.springboot.domain.services.fetcher.Fetcher;
import uk.gov.api.springboot.domain.services.fetcher.FetcherService;

@ExtendWith(MockitoExtension.class)
class FetcherServiceTest {
  private final TestLogger logger = TestLoggerFactory.getTestLogger(FetcherService.class);

  @Mock private Fetcher v1Fetcher;
  @Mock private Fetcher v2Fetcher;
  @Mock private Fetcher v3Fetcher;

  private FetcherService fetcherService;

  @BeforeEach
  void setup() {
    fetcherService = new FetcherService(v1Fetcher, v2Fetcher, v3Fetcher);
  }

  @AfterEach
  void tearDown() {
    logger.clear();
  }

  @Nested
  class Constructors {
    @Test
    void hasNoArgs() {
      assertThat(new FetcherService()).isNotNull();
    }

    @Test
    void hasVarargs() {
      assertThat(new FetcherService(v1Fetcher, v2Fetcher)).isNotNull();
    }
  }

  @Nested
  class Fetch {
    private final Registry.Entry entry = new Registry.Entry(UUID.randomUUID(), "https://foo");

    @Mock private List<Api> expected;

    private List<Api> actual;

    @Nested
    class WhenV1Supported {
      @BeforeEach
      void setup()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenReturn(expected);

        actual = fetcherService.fetch(entry);
      }

      @Test
      void delegatesToV1()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        verify(v1Fetcher).fetch("https://foo");
      }

      @Test
      void returnsApisFromDelegate() {
        assertThat(actual).isEqualTo(expected);
      }

      @Test
      void doesNotContinue() {
        verifyNoInteractions(v2Fetcher, v3Fetcher);
      }
    }

    @Nested
    class WhenOnlyV2Supported {
      @BeforeEach
      void setup()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v2Fetcher.fetch(any())).thenReturn(expected);

        actual = fetcherService.fetch(entry);
      }

      @Test
      void delegatesToV1()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        verify(v1Fetcher).fetch("https://foo");
      }

      @Test
      void delegatesToV2()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        verify(v2Fetcher).fetch("https://foo");
      }

      @Test
      void returnsApisFromDelegate() {
        assertThat(actual).isEqualTo(expected);
      }

      @Test
      void doesNotContinue() {
        verifyNoInteractions(v3Fetcher);
      }
    }

    @Nested
    class WhenSuccessful {
      @Test
      void aMessageIsLogged()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenReturn(expected);
        when(expected.size()).thenReturn(12);

        fetcherService.fetch(entry);

        assertThat(logger)
            .hasLogged(LoggingEvent.info("Retrieved {} APIs from the {} API", 12, entry.id()));
      }
    }

    @Nested
    class WhenNoneSupported {
      @Test
      void throwsClientErrorFetchException()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v2Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v3Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.ClientErrorException.class);
      }

      @Test
      void delegatesInOrder()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v2Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v3Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.ClientErrorException.class);

        var inorder = Mockito.inOrder(v1Fetcher, v2Fetcher, v3Fetcher);
        inorder.verify(v1Fetcher).fetch("https://foo");
        inorder.verify(v2Fetcher).fetch("https://foo");
        inorder.verify(v3Fetcher).fetch("https://foo");
      }

      @Test
      void aMessageIsLogged()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v2Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());
        when(v3Fetcher.fetch(any())).thenThrow(new Fetcher.VersionNotSupportedException());

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.ClientErrorException.class);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.error(
                    "Could not negotiate a version of the contract supported by {}", entry.id()));
      }
    }

    @Nested
    class WhenClientErrorException {
      @Test
      void throwsClientErrorFetchException()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.ClientErrorException());

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.ClientErrorException.class);
      }

      @Test
      void theExceptionIsLogged()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        var exception = new Fetcher.ClientErrorException();
        when(v1Fetcher.fetch(any())).thenThrow(exception);

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.ClientErrorException.class);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.error(
                    exception,
                    "A client error caused retrieval of APIs from %s to fail"
                        .formatted(entry.id())));
      }
    }

    @Nested
    class WhenTemporaryError {
      @Test
      void throwsTemporaryFetchErrorException()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        when(v1Fetcher.fetch(any())).thenThrow(new Fetcher.TemporaryErrorException());

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.TemporaryErrorException.class);
      }

      @Test
      void theExceptionIsLogged()
          throws Fetcher.VersionNotSupportedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException {
        var exception = new Fetcher.TemporaryErrorException();
        when(v1Fetcher.fetch(any())).thenThrow(exception);

        assertThatThrownBy(() -> fetcherService.fetch(entry))
            .isInstanceOf(Fetcher.TemporaryErrorException.class);

        assertThat(logger)
            .hasLogged(
                LoggingEvent.error(
                    exception,
                    "A temporary error caused retrieval of APIs from %s to fail"
                        .formatted(entry.id())));
      }
    }
  }
}
