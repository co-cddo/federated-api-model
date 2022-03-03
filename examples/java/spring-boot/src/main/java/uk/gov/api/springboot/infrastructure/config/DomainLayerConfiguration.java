package uk.gov.api.springboot.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.api.springboot.domain.model.Registry;
import uk.gov.api.springboot.domain.services.fetcher.FetcherService;

@Configuration
public class DomainLayerConfiguration {
  @Bean
  public Registry registry(Registry.Storage storage) {
    return new Registry(storage);
  }

  @Bean
  public FetcherService fetcherService() {
    return new FetcherService();
  }
}
