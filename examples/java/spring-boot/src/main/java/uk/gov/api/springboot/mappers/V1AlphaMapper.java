package uk.gov.api.springboot.mappers;

import java.net.URI;
import org.springframework.stereotype.Component;
import uk.gov.api.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.models.metadata.v1alpha.Data;
import uk.gov.api.springboot.dtos.MetadataDto;

/**
 * Mapper class to convert service-layer Data Transformation Objects (DTOs) to HTTP-layer objects.
 */
@Component
public class V1AlphaMapper {

  /**
   * Convert a {@link MetadataDto} to a v1alpha {@link ApiMetadata}.
   *
   * @return the {@link ApiMetadata}
   */
  public ApiMetadata convert(MetadataDto dto) {
    var apiMetadata = new ApiMetadata();
    apiMetadata.setApiVersion(ApiMetadata.ApiVersion.fromValue(dto.apiVersion()));
    var data = new Data();
    data.setName(dto.name());
    data.setDescription(dto.description());
    data.setUrl(URI.create(dto.url()));
    data.setContact(dto.contact());
    data.setOrganisation(dto.organisation());
    data.setDocumentationUrl(URI.create(dto.documentationUrl()));
    apiMetadata.setData(data);
    return apiMetadata;
  }
}
