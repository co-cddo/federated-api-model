package uk.gov.api.springboot.interfaces;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.api.interfaces.metadata.v1alpha.ApiMetadata;
import uk.gov.api.interfaces.metadata.v1alpha.BulkMetadataResponse;
import uk.gov.api.springboot.application.MetadataService;

@RestController
@RequestMapping("/apis")
public class MetadataController {

  private final MetadataService service;

  public MetadataController(MetadataService service) {
    this.service = service;
  }

  @GetMapping(produces = "application/vnd.uk.gov.api.v1alpha+json")
  public BulkMetadataResponse retrieveAll() {
    List<ApiMetadata> apiMetadata = service.retrieveAll();
    var response = new BulkMetadataResponse();
    response.setApis(apiMetadata);
    response.setApiVersion(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);
    return response;
  }
}
