package uk.gov.api.springboot.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmptyApiStorageTest {

  @Test
  void findAllReturnsEmptyList() {
    var storage = new EmptyApiStorage();

    assertThat(storage.findAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var storage = new EmptyApiStorage();

    var actual = storage.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
