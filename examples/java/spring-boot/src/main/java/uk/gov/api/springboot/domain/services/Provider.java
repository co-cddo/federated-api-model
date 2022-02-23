package uk.gov.api.springboot.domain.services;

import java.util.List;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

public class Provider {

  private final ApiStorage storage;

  public Provider(ApiStorage storage) {
    this.storage = storage;
  }

  public List<Api> retrieveAll() {
    return storage.findAll();
  }
}
