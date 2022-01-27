package uk.gov.api.springboot.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmptyApiRepositoryTest {

  @Test
  void findAllReturnsEmptyList() {
    var repository = new EmptyApiRepository();

    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var repository = new EmptyApiRepository();

    var actual = repository.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
