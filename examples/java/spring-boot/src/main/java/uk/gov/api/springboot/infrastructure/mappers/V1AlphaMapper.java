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
    apiMetadata.setApiVersion(ApiMetadata.ApiVersion.fromValue(api.apiVersion()));
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
}
