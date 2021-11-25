package uk.gov.api.springboot.controllers;

import java.util.List;
import uk.gov.api.springboot.models.metadata.ApiMetadata;
import uk.gov.api.springboot.services.MetadataService;

public class MetadataController {

  private final MetadataService service;

  public MetadataController(MetadataService service) {
    this.service = service;
  }

  public List<ApiMetadata> retrieveAll() {
    return service.retrieveAll();
  }
}
