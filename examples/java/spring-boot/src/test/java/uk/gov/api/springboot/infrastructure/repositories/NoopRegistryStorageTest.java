package uk.gov.api.springboot.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NoopRegistryStorageTest {

  private final NoopRegistryStorage registry = new NoopRegistryStorage();

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
