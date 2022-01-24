package uk.gov.api.springboot.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.api.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.models.metadata.v1alpha.BulkMetadataResponse;
import uk.gov.api.springboot.mappers.V1AlphaMapper;
import uk.gov.api.springboot.services.MetadataService;

@RestController
@RequestMapping("/apis")
public class MetadataController {

  private final MetadataService service;
  private final V1AlphaMapper v1AlphaMapper;

  public MetadataController(MetadataService service, V1AlphaMapper v1AlphaMapper) {
    this.service = service;
    this.v1AlphaMapper = v1AlphaMapper;
  }

  @GetMapping(produces = "application/vnd.uk.gov.api.v1alpha+json")
  public BulkMetadataResponse retrieveAll() {
    List<ApiMetadata> apiMetadata =
        service.retrieveAll().stream().map(v1AlphaMapper::convert).toList();
    var response = new BulkMetadataResponse();
    response.setApis(apiMetadata);
    response.setApiVersion(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);
    return response;
  }
}
