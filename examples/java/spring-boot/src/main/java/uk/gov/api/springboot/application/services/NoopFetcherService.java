package uk.gov.api.springboot.application.services;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.Registry;
import uk.gov.api.springboot.domain.services.fetcher.FetcherService;

@Component
public class NoopFetcherService implements FetcherService {

  @Override
  public List<Api> fetch(Registry.Entry entry) {
    return Collections.emptyList();
  }
}
