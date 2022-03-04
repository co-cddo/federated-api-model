package uk.gov.api.springboot.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.api.springboot.domain.model.Registry;

@Configuration
public class DomainLayerConfiguration {
  @Bean
  public Registry registry(Registry.Storage storage) {
    return new Registry(storage);
  }
}
