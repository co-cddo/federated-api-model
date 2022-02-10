package uk.gov.api.springboot.application.services;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@Service
public class ApiService {

  private final ApiStorage storage;

  public ApiService(ApiStorage storage) {
    this.storage = storage;
  }

  public List<Api> retrieveAll() {
    return storage.findAll();
  }
}
