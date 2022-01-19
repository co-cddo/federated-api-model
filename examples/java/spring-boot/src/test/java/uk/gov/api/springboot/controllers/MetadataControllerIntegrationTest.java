package uk.gov.api.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.restassured.module.jsv.JsonSchemaValidator;
import java.net.URI;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import uk.gov.api.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.models.metadata.v1alpha.Data;
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
      var apiMetadata1 = new ApiMetadata();
      var data1 = new Data();
      data1.setName("API 1");
      apiMetadata1.setData(data1);
      var apiMetadata2 = new ApiMetadata();
      var data2 = new Data();
      data2.setName("API 2");
      apiMetadata2.setData(data2);

      when(service.retrieveAll()).thenReturn(List.of(apiMetadata1, apiMetadata2));

      mockMvc
          .perform(get("/apis"))
          .andExpect(
              jsonPath("$.apis[*].data.name").value(Matchers.containsInAnyOrder("API 1", "API 2")));
    }

    @Test
    void returnsResponseValidatesAgainstSchema() throws Exception {
      when(service.retrieveAll()).thenReturn(List.of(validObject()));

      mockMvc
          .perform(get("/apis"))
          .andExpect(
              content()
                  .string(
                      JsonSchemaValidator.matchesJsonSchemaInClasspath(
                          "schemas/v1alpha/bulk-metadata-response.json")));
    }

    @Test
    void returnsCorrectContentType() throws Exception {
      ResultActions resultActions = mockMvc.perform(get("/apis"));
      assertContentTypeIsCorrect(resultActions);
    }

    @Test
    void returnsCorrectContentTypeWhenRequestingWildcardJson() throws Exception {
      ResultActions resultActions = mockMvc.perform(get("/apis").accept("application/*+json"));
      assertContentTypeIsCorrect(resultActions);
    }

    @Test
    void returnsCorrectContentTypeWhenRequestingV1Alpha() throws Exception {
      ResultActions resultActions =
          mockMvc.perform(get("/apis").accept("application/vnd.uk.gov.api.v1alpha+json"));
      assertContentTypeIsCorrect(resultActions);
    }

    @ParameterizedTest
    @ValueSource(strings = {"text/plain", "application/json"})
    void returns407NotAcceptableIfNegotiationFails(String mediaType) throws Exception {
      mockMvc
          .perform(get("/apis").accept(mediaType))
          .andExpect(status().isNotAcceptable())
          .andExpect(content().string(""));
    }

    private void assertContentTypeIsCorrect(ResultActions resultActions) throws Exception {
      resultActions.andExpect(
          header().string("content-type", "application/vnd.uk.gov.api.v1alpha+json"));
    }
  }

  private static ApiMetadata validObject() {
    var apiMetadata = new ApiMetadata();
    apiMetadata.setApiVersion(ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
    var data = new Data();
    data.setName("Name");
    data.setContact("contact@email");
    data.setDescription("the API description");
    data.setOrganisation("The Org");
    data.setDocumentationUrl(URI.create("https://docs.example"));
    data.setUrl(URI.create("https://api-endpoint.example"));
    apiMetadata.setData(data);
    return apiMetadata;
  }
}
