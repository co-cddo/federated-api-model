package uk.gov.api.springboot.services;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.gov.api.springboot.dtos.MetadataDto;
import uk.gov.api.springboot.repositories.MetadataRepository;

@Service
public class MetadataService {

  private final MetadataRepository repository;

  public MetadataService(MetadataRepository repository) {
    this.repository = repository;
  }

  public List<MetadataDto> retrieveAll() {
    return repository.findAll();
  }
}
