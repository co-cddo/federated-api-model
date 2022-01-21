package uk.gov.api.springboot.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmptyMetadataRepositoryTest {

  @Test
  void findAllReturnsEmptyList() {
    var repository = new EmptyMetadataRepository();

    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var repository = new EmptyMetadataRepository();

    var actual = repository.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
