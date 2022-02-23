package uk.gov.api.springboot.infrastructure;

import static com.github.valfirst.slf4jtest.Assertions.*;

import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ScheduleHandlerTest {

  private final ScheduleHandler handler = new ScheduleHandler();

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
}
