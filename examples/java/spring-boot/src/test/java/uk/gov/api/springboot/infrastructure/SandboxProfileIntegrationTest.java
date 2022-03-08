package uk.gov.api.springboot.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.api.springboot.domain.model.repositories.ApiStorage;

@SpringBootTest
@ActiveProfiles("sandbox")
class SandboxProfileIntegrationTest {
  @Autowired private ApiStorage apiStorage;

  @Test
  void apiStorageIsPreseededWithEntries() {
    assertThat(apiStorage.findAll()).hasSizeGreaterThan(0);
  }
}
