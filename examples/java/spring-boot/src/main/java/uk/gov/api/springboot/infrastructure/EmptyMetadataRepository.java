package uk.gov.api.springboot.infrastructure;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.domain.models.MetadataDao;

@Repository
public class EmptyMetadataRepository implements MetadataRepository {

  @Override
  public List<MetadataDao> findAll() {
    return Collections.emptyList();
  }
}
