package uk.gov.api.springboot.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.api.springboot.models.metadata.ApiMetadata;
import uk.gov.api.springboot.services.MetadataService;

@RestController
@RequestMapping("/apis")
public class MetadataController {

  private final MetadataService service;

  public MetadataController(MetadataService service) {
    this.service = service;
  }

  @GetMapping
  public List<ApiMetadata> retrieveAll() {
    return service.retrieveAll();
  }
}
