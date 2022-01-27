package uk.gov.api.springboot.repositories;

import java.util.List;
import uk.gov.api.springboot.dtos.MetadataDto;

public interface MetadataRepository {

  List<MetadataDto> findAll();
}
