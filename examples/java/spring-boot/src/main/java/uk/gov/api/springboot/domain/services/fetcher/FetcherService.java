package uk.gov.api.springboot.domain.services.fetcher;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.Registry;

/** Core domain logic for fetching lists of APIs from external services. */
public class FetcherService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FetcherService.class);

  private final LinkedHashSet<Fetcher> fetchers;

  /**
   * Construct a {@link FetcherService} with the {@link Fetcher}s in use.
   *
   * <p>NOTE that fetchers should be in order of processing.
   */
  public FetcherService(Fetcher... fetchers) {
    this(toSet(fetchers));
  }

  private FetcherService(LinkedHashSet<Fetcher> fetchers) {
    this.fetchers = fetchers;
  }

  public List<Api> fetch(Registry.Entry entry)
      throws Fetcher.ClientErrorException, Fetcher.TemporaryErrorException {
    for (var fetcher : fetchers) {
      try {
        var fetched = fetcher.fetch(entry.baseUrl());
        LOGGER.info("Retrieved {} APIs from the {} API", fetched.size(), entry.id());
        return fetched;
      } catch (Fetcher.VersionNotSupportedException e) {
        // ignore
      } catch (Fetcher.ClientErrorException e) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error(
              "A client error caused retrieval of APIs from %s to fail".formatted(entry.id()), e);
        }
        throw e;
      } catch (Fetcher.TemporaryErrorException e) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error(
              "A temporary error caused retrieval of APIs from %s to fail".formatted(entry.id()),
              e);
        }
        throw e;
      }
    }

    LOGGER.error("Could not negotiate a version of the contract supported by {}", entry.id());
    throw new Fetcher.ClientErrorException();
  }

  private static LinkedHashSet<Fetcher> toSet(Fetcher... fetchers) {
    return new LinkedHashSet<>(Arrays.asList(fetchers));
  }
}
