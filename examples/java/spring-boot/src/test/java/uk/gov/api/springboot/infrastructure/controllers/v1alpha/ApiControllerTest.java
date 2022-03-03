package uk.gov.api.springboot.infrastructure.controllers.v1alpha;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.application.services.ApiService;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.infrastructure.mappers.V1AlphaMapper;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ApiController;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.BulkMetadataResponse;

@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

  @Nested
  class RetrieveAll {

    @Mock private ApiService service;
    @Mock private V1AlphaMapper v1AlphaMapper;

    @InjectMocks private ApiController controller;

    @Test
    void returnsListOfApiMetadata(@Mock ApiMetadata metadata1, @Mock ApiMetadata metadata2) {
      Api api1 = new Api("name1", null, null, null, null, null);
      Api api2 = new Api("name2", null, null, null, null, null);
      when(service.retrieveAll()).thenReturn(List.of(api1, api2));
      when(v1AlphaMapper.convert(api1)).thenReturn(metadata1);
      when(v1AlphaMapper.convert(api2)).thenReturn(metadata2);

      BulkMetadataResponse actual = controller.retrieveAll();

      assertThat(actual.getApis()).containsExactly(metadata1, metadata2);
      assertThat(actual.getApiVersion())
          .isEqualTo(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);
    }
  }
}
