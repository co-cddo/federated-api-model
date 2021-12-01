package uk.gov.api.springboot.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmptyMetadataRepositoryTest {

  @Test
  void findAllReturnsEmptyList() {
    EmptyMetadataRepository repository = new EmptyMetadataRepository();

    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    EmptyMetadataRepository repository = new EmptyMetadataRepository();

    var actual = repository.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
