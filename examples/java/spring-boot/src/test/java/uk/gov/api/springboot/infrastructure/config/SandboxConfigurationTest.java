package uk.gov.api.springboot.infrastructure.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@ExtendWith(MockitoExtension.class)
class SandboxConfigurationTest {
  @Mock private ApiStorage apiStorage;
  @InjectMocks private SandboxConfiguration configuration;

  @Nested
  class PreSeedApiStorage {
    @Test
    void addsSandboxToStorage() {
      configuration.preSeedApiStorage();

      verify(apiStorage)
          .save(
              new Api(
                  "Federated API Model Sandbox",
                  "A sample deployment of the Federated API Model's reference implementation",
                  "https://federated-api-model-spring-boot-sandbox.london.cloudapps.digital",
                  "api-programme@digital.cabinet-office.gov.uk",
                  "CDDO",
                  "https://github.com/co-cddo/federated-api-model"));
    }
  }
}
