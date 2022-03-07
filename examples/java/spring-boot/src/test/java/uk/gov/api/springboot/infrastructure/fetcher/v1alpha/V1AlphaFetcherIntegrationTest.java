package uk.gov.api.springboot.infrastructure.fetcher.v1alpha;

import static com.github.valfirst.slf4jtest.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.jvt.uuid.Patterns;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.services.fetcher.Fetcher;
import uk.gov.api.springboot.infrastructure.config.MdcFacade;
import uk.gov.api.springboot.infrastructure.mappers.V1AlphaMapper;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.Data;

class V1AlphaFetcherIntegrationTest {
  @ExtendWith(SpringExtension.class)
  @Import({IntegrationTestBase.Config.class, JacksonAutoConfiguration.class})
  @DirtiesContext
  @Nested
  abstract class IntegrationTestBase {
    protected final TestLogger logger = TestLoggerFactory.getTestLogger(V1AlphaFetcher.class);

    @MockBean protected V1AlphaMapper v1AlphaMapper;
    @MockBean protected MdcFacade mdc;

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected MockWebServer server;

    @Autowired protected V1AlphaFetcher fetcher;

    @TestConfiguration
    static class Config {

      @Bean
      public MockWebServer webServer() {
        return new MockWebServer();
      }

      @Bean
      public WebClient webClient() {
        return WebClient.builder().build();
      }

      @Bean
      public V1AlphaFetcher fetcher(
          WebClient webClient, V1AlphaMapper mapper, MdcFacade mdcFacade) {
        return new V1AlphaFetcher(webClient, mapper, mdcFacade);
      }
    }

    @AfterEach
    void tearDown() {
      logger.clear();
    }

    protected List<Api> fetch() {
      return fetcher.fetch(server.url("").toString());
    }

    protected ObjectNode toNode(Api api) {
      var node = objectMapper.createObjectNode();
      node.put("api-version", "api.gov.uk/v1alpha");
      node.putObject("data")
          .put("name", api.name())
          .put("description", api.description())
          .put("url", api.url())
          .put("contact", api.contact())
          .put("organisation", api.organisation())
          .put("documentation-url", api.documentationUrl());
      return node;
    }

    protected String toBody(Api... apis) {
      var container = objectMapper.createObjectNode();
      container.put("api-version", "api.gov.uk/v1alpha");
      var arr = container.putArray("apis");
      for (var api : apis) {
        arr.add(toNode(api));
      }

      return serialise(container);
    }

    protected String serialise(ObjectNode container) {
      try {
        return objectMapper.writeValueAsString(container);
      } catch (JsonProcessingException e) {
        throw new IllegalStateException(e);
      }
    }

    protected MockResponse success(Api... apis) {
      return new MockResponse()
          .setResponseCode(200)
          .setBody(toBody(apis))
          .setHeader("content-type", "application/vnd.uk.gov.api.v1alpha+json")
          .setHeader("correlation-id", "response-correlation-id");
    }

    protected String baseUrl() {
      return "%s/apis".formatted(server.url("").toString());
    }
  }

  @Nested
  class Fetch {
    @Nested
    class InGeneral extends IntegrationTestBase {

      @Test
      void delegatesToMapperFromMethod() {
        var api = fakeApi();
        var expected = new ApiMetadata();
        expected.setApiVersion(ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
        var data = new Data();
        data.setName("The API Name");
        data.setDescription("A longer description about the API");
        data.setUrl(URI.create("https://gov.uk"));
        data.setContact("email@example.com");
        data.setOrganisation("Department");
        data.setDocumentationUrl(URI.create("https://docs"));
        expected.setData(data);

        server.enqueue(success(api));

        fetch();

        verify(v1AlphaMapper, atLeast(1)).from(expected);
      }

      @Test
      void returnsApisWhenSuccessful()
          throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException,
              Fetcher.VersionNotSupportedException {
        var api = fakeApi();

        server.enqueue(success(api));
        when(v1AlphaMapper.from(any())).thenReturn(api);

        var actual = fetch();

        assertThat(actual).containsExactlyInAnyOrder(api);
      }

      @Test
      void itAddsRequestCorrelationIdToMdcFromMdc() {
        server.enqueue(success());
        when(mdc.get(any())).thenReturn("request-correlation-id");

        fetch();

        verify(mdc, atLeast(1)).put("request.v1alpha.correlation-id", "request-correlation-id");
      }

      @Test
      void itRetrievesKeyFromMdc() {
        server.enqueue(success());

        fetch();

        verify(mdc, atLeast(1)).get("correlation-id");
      }

      @Test
      void itAddsResponseCorrelationIdToMdc() {
        server.enqueue(success());

        fetch();

        verify(mdc, atLeast(1)).put("response.v1alpha.correlation-id", "response-correlation-id");
      }

      @Test
      void itLogsNumberOfItemsFetched() {
        server.enqueue(success(fakeApi("Name 0"), fakeApi("Name 1"), fakeApi("Name 2")));

        fetch();

        assertThat(logger)
            .anyThread()
            .hasLogged(LoggingEvent.info("Successfully retrieved {} APIs from {}", 3, baseUrl()));
      }
    }

    @Nested
    class Request extends IntegrationTestBase {
      private RecordedRequest request;

      @BeforeEach
      void setup()
          throws InterruptedException, Fetcher.ClientErrorException,
              Fetcher.TemporaryErrorException, Fetcher.VersionNotSupportedException {
        when(mdc.get(any())).thenReturn("mdc-correlation-id");
        server.enqueue(success());

        fetch();

        request = server.takeRequest(100, TimeUnit.MILLISECONDS);
        assertThat(request).isNotNull();
      }

      @Test
      void isGet() {
        assertThat(request.getMethod()).isEqualTo("GET");
      }

      @Test
      void isCorrectPath() {
        assertThat(request.getPath()).isEqualTo("/apis");
      }

      @Test
      void hasCorrelationId() {
        assertThat(request.getHeader("correlation-id")).isEqualTo("mdc-correlation-id");
      }

      @Test
      void hasAccept() {
        assertThat(request.getHeader("accept"))
            .isEqualTo("application/vnd.uk.gov.api.v1alpha+json");
      }
    }

    @Nested
    class WhenNotFound extends IntegrationTestBase {

      @Test
      void throwsVersionNotSupportedException() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.VersionNotSupportedException.class);
      }

      @Test
      void itLogs() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.VersionNotSupportedException.class);

        assertThat(logger)
            .anyThread()
            .hasLogged(LoggingEvent.warn("API Version not supported by {}, HTTP 406", baseUrl()));
      }
    }

    @Nested
    class WhenNotAcceptable extends IntegrationTestBase {

      @Test
      void throwsVersionNotSupportedException() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_ACCEPTABLE.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.VersionNotSupportedException.class);
      }

      @Test
      void itLogs() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_ACCEPTABLE.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.VersionNotSupportedException.class);

        assertThat(logger)
            .anyThread()
            .hasLogged(LoggingEvent.warn("API Version not supported by {}, HTTP 406", baseUrl()));
      }
    }

    @Nested
    class WhenBadRequest extends IntegrationTestBase {

      @BeforeEach
      void setup() {
        var response = objectMapper.createObjectNode();
        response.put("error", "invalid_request");
        response.put("error-description", "the description");

        server.enqueue(
            new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setHeader("content-type", "application/vnd.uk.gov.api.v1alpha+json")
                .setHeader("correlation-id", UUID.randomUUID().toString())
                .setBody(serialise(response)));
      }

      @Test
      void throwsClientErrorException() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);
      }

      @Test
      void itLogsErrorAndErrorDescriptionFromResponseBody() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);
        System.err.println(logger.getAllLoggingEvents());
        assertThat(logger)
            .anyThread()
            .hasLogged(
                LoggingEvent.error(
                    "Bad request returned by {}, could be contract issue? {error: `{}`, error-description: `{}`}",
                    baseUrl(),
                    "invalid_request",
                    "the description"));
      }

      @Test
      void itAddsResponseCorrelationIdToMdc() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);

        verify(mdc).put(eq("response.v1alpha.correlation-id"), matches(Patterns.UUID));
      }
    }

    @Nested
    class If5xxResponse extends IntegrationTestBase {

      @Test
      void throwsTemporaryErrorException() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_GATEWAY.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.TemporaryErrorException.class);
      }

      @Test
      void itLogs() {
        server.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_GATEWAY.value()));

        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.TemporaryErrorException.class);

        assertThat(logger)
            .anyThread()
            .hasLogged(
                LoggingEvent.warn("A server error (HTTP {}) was returned by {}", 502, baseUrl()));
      }
    }

    @Nested
    class WhenBadApiVersion extends IntegrationTestBase {

      @BeforeEach
      void setup() {
        var response = objectMapper.createObjectNode();
        response.put("api-version", "not_valid");

        server.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/vnd.uk.gov.api.v1alpha+json")
                .setBody(serialise(response)));
      }

      @Test
      void throwsClientErrorException() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);
      }

      @Test
      void itLogs() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);

        assertThat(logger)
            .anyThread()
            .hasLogged(
                LoggingEvent.error(
                    "Bad response body returned by {}, could be contract issue?", baseUrl()));
      }
    }

    @Nested
    class WhenEmptyResponseBody extends IntegrationTestBase {

      @BeforeEach
      void setup() {
        var response = objectMapper.createObjectNode();

        server.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/vnd.uk.gov.api.v1alpha+json")
                .setBody(serialise(response)));
      }

      @Test
      void throwsClientErrorException() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);
      }

      @Test
      void itLogs() {
        assertThatThrownBy(() -> fetch()).isInstanceOf(Fetcher.ClientErrorException.class);

        assertThat(logger)
            .anyThread()
            .hasLogged(
                LoggingEvent.error(
                    "Bad response body returned by {}, could be contract issue?", baseUrl()));
      }
    }
  }

  private static Api fakeApi(String name) {
    return new Api(
        name,
        "A longer description about the API",
        "https://gov.uk",
        "email@example.com",
        "Department",
        "https://docs");
  }

  private static Api fakeApi() {
    return fakeApi("The API Name");
  }
}
