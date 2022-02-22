package uk.gov.api.springboot.domain.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@ExtendWith(MockitoExtension.class)
class ProviderTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private Provider provider;

    @Mock private ApiStorage storage;

    @Test
    void delegates(@Mock List<Api> apis) {
      when(storage.findAll()).thenReturn(apis);

      List<Api> actual = provider.retrieveAll();

      assertThat(actual).isEqualTo(apis);
    }
  }
}
