package uk.gov.api.springboot.infrastructure.config;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(properties = "apis-fetch-rate.in.milliseconds = 500")
class ScheduledHandlerIntegrationTest {

  @SpyBean private ScheduleHandler handler;

  @Test
  void testFetchAndSaveApisCalledOnSchedule() {
    await()
        .atMost(Durations.ONE_SECOND)
        .untilAsserted(() -> verify(handler, atLeast(1)).fetchAndSaveApis());
  }
}
