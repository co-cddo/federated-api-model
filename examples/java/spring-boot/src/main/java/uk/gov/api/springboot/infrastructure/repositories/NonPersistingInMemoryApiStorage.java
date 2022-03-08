package uk.gov.api.springboot.infrastructure.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@Repository
public class NonPersistingInMemoryApiStorage implements ApiStorage {

  private final List<Api> apis = new ArrayList<>();

  @Override
  public void save(Api api) {
    apis.add(api);
  }

  @Override
  public List<Api> findAll() {
    return Collections.unmodifiableList(apis);
  }
}
