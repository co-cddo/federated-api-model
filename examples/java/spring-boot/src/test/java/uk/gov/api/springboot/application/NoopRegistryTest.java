package uk.gov.api.springboot.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NoopRegistryTest {

  private final NoopRegistry registry = new NoopRegistry();

  @Test
  void findAllReturnsEmptyList() {
    assertThat(registry.retrieveAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var actual = registry.retrieveAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
