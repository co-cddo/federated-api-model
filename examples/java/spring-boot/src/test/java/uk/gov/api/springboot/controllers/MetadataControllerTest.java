package uk.gov.api.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import models.metadata.ApiMetadata;
import models.metadata.BulkMetadataResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.services.MetadataService;

@ExtendWith(MockitoExtension.class)
class MetadataControllerTest {

  @Nested
  class RetrieveAll {

    @Mock private MetadataService service;

    @InjectMocks private MetadataController controller;

    @Test
    void returnsListOfApiMetadata(@Mock List<ApiMetadata> apiMetadata) {
      when(service.retrieveAll()).thenReturn(apiMetadata);

      BulkMetadataResponse actual = controller.retrieveAll();

      assertThat(actual.getApis()).isEqualTo(apiMetadata);
      assertThat(actual.getApiVersion())
          .isEqualTo(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);
    }
  }
}
