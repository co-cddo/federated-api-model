package uk.gov.api.springboot.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import uk.gov.api.springboot.domain.model.Api;

class NonPersistingInMemoryApiStorageTest {

  private final NonPersistingInMemoryApiStorage storage = new NonPersistingInMemoryApiStorage();

  @Test
  void startsEmpty() {
    assertThat(storage.findAll()).isEmpty();
  }

  @Test
  void canSaveItems() {
    var api = fakeApi();
    storage.save(api);

    assertThat(storage.findAll()).containsExactly(api);
  }

  @Test
  void findAllReturnsUnmodifiableList() {
    var actual = storage.findAll();

    assertThatThrownBy(() -> actual.add(null)).isInstanceOf(UnsupportedOperationException.class);
  }

  private Api fakeApi() {
    return new Api(
        "name 1",
        "description 1",
        "https://www.example.foo",
        "contact 1",
        "org 1",
        "https://www.exampledocs.foo");
  }
}
