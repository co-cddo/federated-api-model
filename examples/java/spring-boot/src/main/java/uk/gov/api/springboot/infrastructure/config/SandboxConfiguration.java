package uk.gov.api.springboot.infrastructure.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@Profile("sandbox")
@Configuration
public class SandboxConfiguration {

  private final ApiStorage apiStorage;

  public SandboxConfiguration(ApiStorage apiStorage) {
    this.apiStorage = apiStorage;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void preSeedApiStorage() {
    apiStorage.save(
        new Api(
            "Federated API Model Sandbox",
            "A sample deployment of the Federated API Model's reference implementation",
            "https://federated-api-model-spring-boot-sandbox.london.cloudapps.digital",
            "api-programme@digital.cabinet-office.gov.uk",
            "Central Digital and Data Office",
            "https://github.com/co-cddo/federated-api-model"));
  }
}
