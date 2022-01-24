package uk.gov.api.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.models.metadata.v1alpha.BulkMetadataResponse;
import uk.gov.api.springboot.dtos.MetadataDto;
import uk.gov.api.springboot.mappers.V1AlphaMapper;
import uk.gov.api.springboot.services.MetadataService;

@ExtendWith(MockitoExtension.class)
class MetadataControllerTest {

  @Nested
  class RetrieveAll {

    @Mock private MetadataService service;
    @Mock private V1AlphaMapper v1AlphaMapper;

    @InjectMocks private MetadataController controller;

    @Test
    void returnsListOfApiMetadata(@Mock ApiMetadata metadata1, @Mock ApiMetadata metadata2) {
      MetadataDto dto1 = new MetadataDto("v1", null, null, null, null, null, null);
      MetadataDto dto2 = new MetadataDto("v2", null, null, null, null, null, null);
      when(service.retrieveAll()).thenReturn(List.of(dto1, dto2));
      when(v1AlphaMapper.convert(dto1)).thenReturn(metadata1);
      when(v1AlphaMapper.convert(dto2)).thenReturn(metadata2);

      BulkMetadataResponse actual = controller.retrieveAll();

      assertThat(actual.getApis()).containsExactly(metadata1, metadata2);
      assertThat(actual.getApiVersion())
          .isEqualTo(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);
    }
  }
}
