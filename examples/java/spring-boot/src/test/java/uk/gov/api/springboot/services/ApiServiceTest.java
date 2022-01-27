package uk.gov.api.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.dtos.Api;
import uk.gov.api.springboot.repositories.ApiRepository;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private ApiService service;

    @Mock private ApiRepository repository;

    @Test
    void delegates(@Mock List<Api> apis) {
      when(repository.findAll()).thenReturn(apis);

      List<Api> actual = service.retrieveAll();

      assertThat(actual).isEqualTo(apis);
    }
  }
}
