package uk.gov.api.springboot.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.dtos.MetadataDto;

@Repository
public class EmptyMetadataRepository implements MetadataRepository {

  @Override
  public List<MetadataDto> findAll() {
    return Collections.emptyList();
  }
}
