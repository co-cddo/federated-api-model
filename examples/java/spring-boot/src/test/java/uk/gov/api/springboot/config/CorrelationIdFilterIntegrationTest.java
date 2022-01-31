package uk.gov.api.springboot.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
public class CorrelationIdFilterIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void returnsCorrelationIdInResponseIfValidUuidProvided() throws Exception {
    String uuid = UUID.randomUUID().toString();
    mockMvc
            .perform(get("/anything").header("correlation-id", uuid))
            .andExpect(header().string("correlation-id", uuid));
  }


}
