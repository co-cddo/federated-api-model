package uk.gov.api.springboot.infrastructure.fetcher.v1alpha;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.services.fetcher.Fetcher;
import uk.gov.api.springboot.infrastructure.config.MdcFacade;
import uk.gov.api.springboot.infrastructure.mappers.V1AlphaMapper;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.BulkMetadataResponse;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ErrorResponse;

@Component
public class V1AlphaFetcher implements Fetcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(V1AlphaFetcher.class);
  private static final String CORRELATION_ID_HEADER = "correlation-id";

  private final WebClient webClient;
  private final V1AlphaMapper mapper;
  private final MdcFacade mdc;

  public V1AlphaFetcher(WebClient webClient, V1AlphaMapper mapper, MdcFacade mdc) {
    this.webClient = webClient;
    this.mapper = mapper;
    this.mdc = mdc;
  }

  @Override
  public List<Api> fetch(String baseUrl) {
    String url = baseUrl + "/apis";
    String correlationId = mdc.get(MdcFacade.CORRELATION_ID);
    mdc.put("request.v1alpha.correlation-id", correlationId);

    ResponseEntity<BulkMetadataResponse> entity =
        webClient
            .get()
            .uri(url)
            .accept(MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json"))
            .header(CORRELATION_ID_HEADER, correlationId)
            .retrieve()
            .onStatus(eq(HttpStatus.NOT_FOUND), handleNotFound(url))
            .onStatus(eq(HttpStatus.NOT_ACCEPTABLE), handleNotAcceptable(url))
            .onStatus(eq(HttpStatus.BAD_REQUEST), handleBadRequest(url))
            .onStatus(HttpStatus::is5xxServerError, handle5xxServerError(url))
            .toEntity(BulkMetadataResponse.class)
            .onErrorMap(handleError(url))
            .block();
    if (entity == null) {
      logContractIssue(url);
      throw new ClientErrorException();
    }
    var body = entity.getBody();
    if (body == null) {
      logContractIssue(url);
      throw new ClientErrorException();
    }
    if (!BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA.equals(body.getApiVersion())) {
      logContractIssue(url);
      throw new ClientErrorException();
    }

    mdc.put("response.v1alpha.correlation-id", entity.getHeaders().getFirst(CORRELATION_ID_HEADER));

    var apis = body.getApis().stream().map(mapper::from).toList();
    LOGGER.info("Successfully retrieved {} APIs from {}", apis.size(), url);
    return apis;
  }

  private static Function<Throwable, Throwable> handleError(String url) {
    return exception -> {
      if ((exception instanceof TemporaryErrorException)
          || (exception instanceof ClientErrorException)
          || (exception instanceof VersionNotSupportedException)) {
        return exception;
      }
      // this could be for other reasons?
      logContractIssue(url);
      return new ClientErrorException();
    };
  }

  private static void logContractIssue(String url) {
    LOGGER.error("Bad response body returned by {}, could be contract issue?", url);
  }

  private static Predicate<HttpStatus> eq(HttpStatus status) {
    return s -> s.equals(status);
  }

  private static Function<ClientResponse, Mono<? extends Throwable>> handleNotFound(String url) {
    return err -> {
      LOGGER.warn("API Version not supported by {}, HTTP 406", url);
      return Mono.error(new VersionNotSupportedException());
    };
  }

  private static Function<ClientResponse, Mono<? extends Throwable>> handleNotAcceptable(
      String url) {
    return err -> {
      LOGGER.warn("API Version not supported by {}, HTTP 406", url);
      return Mono.error(new VersionNotSupportedException());
    };
  }

  private Function<ClientResponse, Mono<? extends Throwable>> handleBadRequest(String url) {
    return err ->
        err.bodyToMono(ErrorResponse.class).handle(handleBadRequestErrorResponse(url, err));
  }

  private BiConsumer<ErrorResponse, SynchronousSink<Throwable>> handleBadRequestErrorResponse(
      String url, ClientResponse err) {
    return (errorResponse, sink) -> {
      mdc.put(
          "response.v1alpha.correlation-id", err.headers().header(CORRELATION_ID_HEADER).get(0));
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(
            "Bad request returned by {}, could be contract issue? {error: `{}`, error-description: `{}`}",
            url,
            errorResponse.getError().value(),
            errorResponse.getErrorDescription());
      }
      sink.error(new ClientErrorException());
    };
  }

  private static Function<ClientResponse, Mono<? extends Throwable>> handle5xxServerError(
      String url) {
    return err -> {
      LOGGER.warn("A server error (HTTP {}) was returned by {}", err.rawStatusCode(), url);
      return Mono.error(new TemporaryErrorException());
    };
  }
}
