package uk.gov.api.springboot.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistryTest {
  @Mock private Registry.Storage storage;

  @InjectMocks private Registry registry;

  @Nested
  class RetrieveAll {
    @Test
    void delegates() {
      registry.retrieveAll();

      verify(storage).retrieveAll();
    }

    @Test
    void returnsFromDelegate(@Mock List<Registry.Entry> entries) {
      when(storage.retrieveAll()).thenReturn(entries);

      var actual = registry.retrieveAll();

      assertThat(actual).isEqualTo(entries);
    }
  }
}
