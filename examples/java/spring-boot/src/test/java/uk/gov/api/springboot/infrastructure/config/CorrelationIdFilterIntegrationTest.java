package uk.gov.api.springboot.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import io.restassured.module.jsv.JsonSchemaValidator;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CorrelationIdFilterIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void returnsCorrelationIdInResponseIfValidUuidProvided() throws Exception {
    String correlationId = UUID.randomUUID().toString();
    mockMvc
        .perform(get("/endpoint").header("correlation-id", correlationId))
        .andExpect(header().string("correlation-id", correlationId));
  }

  @Test
  void returns400IfInvalidUuidProvided() throws Exception {
    mockMvc
        .perform(get("/endpoint").header("correlation-id", "invalid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("invalid_request"));
  }

  @Test
  void matchesSchemaWhenInvalidCorrelationIdIsProvided() throws Exception {
    mockMvc
        .perform(get("/endpoint").header("correlation-id", "not-valid"))
        .andExpect(
            content()
                .string(
                    JsonSchemaValidator.matchesJsonSchemaInClasspath(
                        "schemas/v1alpha/error-response.json")));
  }

  @Test
  void correlationIdIsGeneratedIfNoneProvided() throws Exception {
    mockMvc
        .perform(get("/endpoint"))
        .andExpect(status().isNotFound())
        .andExpect(
            header()
                .string(
                    "correlation-id",
                    matchesRegex(
                        "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")));
  }

  @Test
  void notAcceptableStatusIsReturnedIfNegotiationFails() throws Exception {
    mockMvc
        .perform(get("/endpoint").accept("in/valid").header("correlation-id", "not-valid"))
        .andExpect(status().isNotAcceptable())
        .andExpect(
            result -> {
              var response = result.getResponse();
              assertThat(response.getContentType()).isNull();
              assertThat(response.getContentLength()).isZero();
              assertThat(response.getContentAsString()).isEmpty();
            });
  }
}
