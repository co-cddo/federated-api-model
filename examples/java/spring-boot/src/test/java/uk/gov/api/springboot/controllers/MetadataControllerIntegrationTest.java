package uk.gov.api.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.api.springboot.models.metadata.ApiMetadata;
import uk.gov.api.springboot.services.MetadataService;

@AutoConfigureMockMvc
@WebMvcTest(MetadataController.class)
@ExtendWith(SpringExtension.class)
class MetadataControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private MetadataService service;

  @Nested
  class RetrieveAll {

    @Test
    void returns200() throws Exception {
      mockMvc.perform(get("/apis")).andExpect(status().isOk());
    }

    @Test
    void mockDataIsReturned() throws Exception {
      ApiMetadata apiMetadata1 = new ApiMetadata();
      ApiMetadata apiMetadata2 = new ApiMetadata();
      apiMetadata1.setApiVersion("alpha 1");
      apiMetadata2.setApiVersion("alpha 2");

      when(service.retrieveAll()).thenReturn(List.of(apiMetadata1, apiMetadata2));

      mockMvc
          .perform(get("/apis"))
          .andExpect(
              jsonPath("[*].api-version").value(Matchers.containsInAnyOrder("alpha 1", "alpha 2")));
    }
  }
}
