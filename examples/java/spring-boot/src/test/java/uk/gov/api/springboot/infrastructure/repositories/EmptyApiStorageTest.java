package uk.gov.api.springboot.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmptyApiStorageTest {

  private final EmptyApiStorage storage = new EmptyApiStorage();

  @Test
  void findAllReturnsEmptyList() {
    assertThat(storage.findAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var actual = storage.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
