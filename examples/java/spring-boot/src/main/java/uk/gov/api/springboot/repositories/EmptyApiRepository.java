package uk.gov.api.springboot.repositories;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import uk.gov.api.springboot.dtos.Api;

@Repository
public class EmptyApiRepository implements ApiRepository {

  @Override
  public List<Api> findAll() {
    return Collections.emptyList();
  }
}
