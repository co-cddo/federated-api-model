package uk.gov.api.springboot.infrastructure;

import static com.github.valfirst.slf4jtest.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.Registry;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;
import uk.gov.api.springboot.domain.services.fetcher.Fetcher;
import uk.gov.api.springboot.domain.services.fetcher.FetcherService;

@ExtendWith(MockitoExtension.class)
class ScheduleHandlerTest {

  @Mock private Registry registry;
  @Mock private ApiStorage storage;
  @Mock private FetcherService fetcherService;
  @InjectMocks private ScheduleHandler handler;

  @Nested
  class Log {

    private final TestLogger logger = TestLoggerFactory.getTestLogger(ScheduleHandler.class);

    @AfterEach
    void tearDown() {
      logger.clear();
    }

    @Test
    void messageIsLoggedWhenScheduledTaskIsStarted() {
      handler.fetchAndSaveApis();

      assertThat(logger).hasLogged(LoggingEvent.info("Fetching and saving APIS"));
    }

    @Test
    void messageIsLoggedWhenScheduledTaskIsFinished() {
      handler.fetchAndSaveApis();

      assertThat(logger).hasLogged(LoggingEvent.info("Finished fetching and saving APIs"));
    }
  }

  @Test
  void registryEntriesArePassedToFetcher()
      throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException {
    var entry = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example");
    when(registry.retrieveAll()).thenReturn(List.of(entry));

    handler.fetchAndSaveApis();

    verify(fetcherService).fetch(entry);
  }

  @Test
  void apisAreSavedToStorage()
      throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException {
    var entry = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example-one");
    when(registry.retrieveAll()).thenReturn(List.of(entry));
    Api api = new Api(null, null, null, null, null, null);
    when(fetcherService.fetch(any())).thenReturn(List.of(api));

    handler.fetchAndSaveApis();

    verify(storage).save(api);
  }

  @Test
  void clientErrorFetchExceptionsDoNotInterruptFlow()
      throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException {
    var entry0 = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example-one");
    var entry1 = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example-one");
    when(registry.retrieveAll()).thenReturn(List.of(entry0, entry1));

    Api api = new Api(null, null, null, null, null, null);
    when(fetcherService.fetch(any()))
        .thenThrow(new Fetcher.ClientErrorException())
        .thenReturn(List.of(api));

    handler.fetchAndSaveApis();

    verify(storage).save(api);
  }

  @Test
  void temporaryFetchErrorExceptionsDoNotInterruptFlow()
      throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException {
    var entry0 = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example-one");
    var entry1 = new Registry.Entry(UUID.randomUUID(), "https://api-endpoint.example-one");
    when(registry.retrieveAll()).thenReturn(List.of(entry0, entry1));

    Api api = new Api(null, null, null, null, null, null);
    when(fetcherService.fetch(any()))
        .thenThrow(new Fetcher.TemporaryErrorException())
        .thenReturn(List.of(api));

    handler.fetchAndSaveApis();

    verify(storage).save(api);
  }
}
