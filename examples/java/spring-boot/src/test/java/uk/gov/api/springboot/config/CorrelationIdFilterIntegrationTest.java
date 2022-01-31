package uk.gov.api.springboot.config;

import io.restassured.module.jsv.JsonSchemaValidator;
import me.jvt.spring.ContentNegotiator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CorrelationIdFilterIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ContentNegotiator contentNegotiator;

  @Test
  void returnsCorrelationIdInResponseIfValidUuidProvided() throws Exception {
    String uuid = UUID.randomUUID().toString();
    mockMvc
            .perform(get("/endpoint").header("correlation-id", uuid))
            .andExpect(header().string("correlation-id", uuid));
  }

  @Test
  void returns400IfInvalidUuidProvided() throws Exception {
    when(contentNegotiator.negotiate((HttpServletRequest) any()))
            .thenReturn(MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json"));

    String correlationId = "invalid";
    mockMvc
            .perform(get("/endpoint").header("correlation-id", correlationId))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("invalid_request"));
  }

  @Test
  void matchesSchemaWhenInvalidCorrelationIdIsProvided() throws Exception {
    when(contentNegotiator.negotiate((HttpServletRequest) any()))
            .thenReturn(MediaType.valueOf("application/vnd.uk.gov.api.v1alpha+json"));

    mockMvc
            .perform(get("/endpoint").header("correlation-id", "not-valid"))
            .andExpect(
                    content()
                            .string(
                                    JsonSchemaValidator.matchesJsonSchemaInClasspath(
                                            "schemas/v1alpha/error-response.json")));
  }


}
