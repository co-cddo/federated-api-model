package uk.gov.api.springboot.domain.model.repositories;

import java.util.List;
import uk.gov.api.springboot.domain.model.Api;

public interface ApiStorage {

  void save(Api api);

  List<Api> findAll();
}
