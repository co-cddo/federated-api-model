package uk.gov.api.springboot.repositories;

import java.util.List;
import uk.gov.api.springboot.dtos.Api;

public interface ApiRepository {

  List<Api> findAll();
}
