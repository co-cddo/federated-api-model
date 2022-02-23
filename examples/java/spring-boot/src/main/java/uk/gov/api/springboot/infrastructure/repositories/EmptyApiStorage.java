package uk.gov.api.springboot.infrastructure.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@Repository
public class EmptyApiStorage implements ApiStorage {

  @Override
  public void save(Api api) {
    // We'll implement this in a separate step, by just storing a list in memory to begin
    // with.
  }

  @Override
  public List<Api> findAll() {
    return Collections.emptyList();
  }
}
