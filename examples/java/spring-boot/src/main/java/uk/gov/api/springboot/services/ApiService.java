package uk.gov.api.springboot.services;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.gov.api.springboot.dtos.Api;
import uk.gov.api.springboot.repositories.ApiRepository;

@Service
public class ApiService {

  private final ApiRepository repository;

  public ApiService(ApiRepository repository) {
    this.repository = repository;
  }

  public List<Api> retrieveAll() {
    return repository.findAll();
  }
}
