package uk.gov.api.springboot.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleHandler.class);

  @Scheduled(fixedRateString = "${apis-fetch-rate.in.milliseconds}")
  public void fetchAndSaveApis() {
    LOGGER.info("Fetching and saving APIS");

    LOGGER.info("Finished fetching and saving APIs");
  }
}
