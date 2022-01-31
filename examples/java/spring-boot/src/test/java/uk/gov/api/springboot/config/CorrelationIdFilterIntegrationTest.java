package uk.gov.api.springboot.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CorrelationIdFilterIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void returnsCorrelationIdInResponseIfValidUuidProvided() throws Exception {
    String correlationId = UUID.randomUUID().toString();
    mockMvc
        .perform(get("/anything").header("correlation-id", correlationId))
        .andExpect(header().string("correlation-id", correlationId));
  }
}
