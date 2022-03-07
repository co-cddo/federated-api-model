package uk.gov.api.springboot.infrastructure.mappers;

import java.net.URI;
import org.springframework.stereotype.Component;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.Data;

/** Mapper class to convert Domain objects to Infrastructure objects.. */
@Component
public class V1AlphaMapper {

  /**
   * Convert a {@link Api} to a v1alpha {@link ApiMetadata}.
   *
   * @return the {@link ApiMetadata}
   */
  public ApiMetadata convert(Api api) {
    var apiMetadata = new ApiMetadata();
    apiMetadata.setApiVersion(ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
    var data = new Data();
    data.setName(api.name());
    data.setDescription(api.description());
    data.setUrl(URI.create(api.url()));
    data.setContact(api.contact());
    data.setOrganisation(api.organisation());
    data.setDocumentationUrl(URI.create(api.documentationUrl()));
    apiMetadata.setData(data);
    return apiMetadata;
  }

  /**
   * Convert a v1alpha {@link ApiMetadata} HTTP object to a {@link Api} object.
   *
   * @param apiMetadata the {@link ApiMetadata} to convert
   * @return the {@link Api}
   */
  public Api from(ApiMetadata apiMetadata) {
    return new Api(
        apiMetadata.getData().getName(),
        apiMetadata.getData().getDescription(),
        apiMetadata.getData().getUrl().toString(),
        apiMetadata.getData().getContact(),
        apiMetadata.getData().getOrganisation(),
        apiMetadata.getData().getDocumentationUrl().toString());
  }
}
