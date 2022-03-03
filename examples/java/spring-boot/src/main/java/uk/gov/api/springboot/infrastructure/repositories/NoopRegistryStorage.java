package uk.gov.api.springboot.infrastructure.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.domain.model.Registry;

@Repository
public class NoopRegistryStorage implements Registry.Storage {
  @Override
  public List<Registry.Entry> retrieveAll() {
    return Collections.emptyList();
  }
}
