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
import uk.gov.api.springboot.dtos.MetadataDto;
import uk.gov.api.springboot.mappers.V1AlphaMapper;
import uk.gov.api.springboot.services.MetadataService;

@AutoConfigureMockMvc
@WebMvcTest(MetadataController.class)
@ExtendWith(SpringExtension.class)
class MetadataControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private MetadataService service;
  @MockBean private V1AlphaMapper v1AlphaMapper;

  @Nested
  class RetrieveAll {

    @Test
    void returns200() throws Exception {
      mockMvc.perform(get("/apis")).andExpect(status().isOk());
    }

    @Test
    void mockDataIsReturned() throws Exception {
      var dto1 = validDto("API 1");
      var dto2 = validDto("API 2");
      var response1 = validApiMetadata("API 1");
      var response2 = validApiMetadata("API 2");
      when(service.retrieveAll()).thenReturn(List.of(dto1, dto2));
      when(v1AlphaMapper.convert(dto1)).thenReturn(response1);
      when(v1AlphaMapper.convert(dto2)).thenReturn(response2);

      mockMvc
          .perform(get("/apis"))
          .andExpect(
              jsonPath("$.apis[*].data.name").value(Matchers.containsInAnyOrder("API 1", "API 2")));
    }

    @Test
    void returnsResponseValidatesAgainstSchema() throws Exception {
      when(service.retrieveAll()).thenReturn(List.of(validDto()));
      var dto = validDto();
      var response = validApiMetadata();
      when(v1AlphaMapper.convert(dto)).thenReturn(response);

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

  private static MetadataDto validDto() {
    return validDto("Name");
  }

  private static MetadataDto validDto(String name) {
    return new MetadataDto(
        "api.gov.uk/v1alpha",
        name,
        "the API description",
        "https://api-endpoint.example",
        "contact@email",
        "The Org",
        "https://docs.example");
  }

  private static ApiMetadata validApiMetadata() {
    return validApiMetadata("Name");
  }

  private static ApiMetadata validApiMetadata(String name) {
    var apiMetadata = new ApiMetadata();
    apiMetadata.setApiVersion(ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
    var data = new Data();
    data.setName(name);
    data.setContact("contact@email");
    data.setDescription("the API description");
    data.setOrganisation("The Org");
    data.setDocumentationUrl(URI.create("https://docs.example"));
    data.setUrl(URI.create("https://api-endpoint.example"));
    apiMetadata.setData(data);
    return apiMetadata;
  }
}
