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
import uk.gov.api.springboot.dtos.MetadataDto;
import uk.gov.api.springboot.repositories.MetadataRepository;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private MetadataService service;

    @Mock private MetadataRepository repository;

    @Test
    void delegates(@Mock List<MetadataDto> metadataDtos) {
      when(repository.findAll()).thenReturn(metadataDtos);

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).isEqualTo(metadataDtos);
    }
  }
}
