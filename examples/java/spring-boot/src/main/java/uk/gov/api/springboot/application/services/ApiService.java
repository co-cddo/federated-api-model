package uk.gov.api.springboot.application.services;

import org.springframework.stereotype.Service;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;
import uk.gov.api.springboot.domain.services.Provider;

@Service
public class ApiService extends Provider {

  public ApiService(ApiStorage storage) {
    super(storage);
  }
}
