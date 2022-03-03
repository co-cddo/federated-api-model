package uk.gov.api.springboot.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import uk.gov.api.springboot.domain.model.Registry;

class NoopFetcherServiceTest {

  private final NoopFetcherService fetcherService = new NoopFetcherService();

  @Test
  void fetchReturnsAnEmptyList() {
    assertThat(fetcherService.fetch(null)).isEmpty();
  }

  @Test
  void fetchReturnsUnmodifiableList() {
    var registryEntry = new Registry.Entry(UUID.randomUUID(), "www.example.com");

    var actual = fetcherService.fetch(registryEntry);

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
