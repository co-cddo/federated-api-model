package uk.gov.api.springboot.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.api.springboot.domain.model.Registry;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;
import uk.gov.api.springboot.domain.services.fetcher.Fetcher;
import uk.gov.api.springboot.domain.services.fetcher.FetcherService;

@Component
public class ScheduleHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleHandler.class);
  private final FetcherService fetcherService;
  private final Registry registry;
  private final ApiStorage storage;

  public ScheduleHandler(FetcherService fetcherService, Registry registry, ApiStorage storage) {
    this.fetcherService = fetcherService;
    this.registry = registry;
    this.storage = storage;
  }

  @Scheduled(fixedRateString = "${apis-fetch-rate.in.milliseconds}")
  public void fetchAndSaveApis() {
    LOGGER.info("Fetching and saving APIS");

    // Save each list of APIs individually rather than flattening the list, so we don't have to wait
    // for all lists to be fetched before we start saving.
    registry
        .retrieveAll()
        .forEach(
            entry -> {
              try {
                fetcherService.fetch(entry).forEach(storage::save);
              } catch (Fetcher.ClientErrorException | Fetcher.TemporaryErrorException e) {
                // ignored, and logged elsewhere
              }
            });

    LOGGER.info("Finished fetching and saving APIs");
  }
}
