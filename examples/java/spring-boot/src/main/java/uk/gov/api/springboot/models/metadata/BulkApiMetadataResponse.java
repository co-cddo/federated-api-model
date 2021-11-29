package uk.gov.api.springboot.models.metadata;

import java.util.Collections;
import java.util.List;

public class BulkApiMetadataResponse {

  private List<ApiMetadata> apis = Collections.emptyList();

  public List<ApiMetadata> getApis() {
    return apis;
  }

  public void setApis(List<ApiMetadata> apis) {
    if (apis == null) {
      apis = Collections.emptyList();
    }
    this.apis = apis;
  }
}
