package uk.gov.api.springboot.infrastructure.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.springboot.infrastructure.models.metadata.v1alpha.Data;

class V1AlphaMapperTest {

  private final V1AlphaMapper mapper = new V1AlphaMapper();

  @Nested
  class ConvertApi {

    private ApiMetadata actual;

    @BeforeEach
    void setup() {
      var api = getApi();

      actual = mapper.convert(api);
    }

    @Test
    void mapsApiVersion() {
      assertApiVersion(actual, ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
    }

    @Test
    void mapsName() {
      assertName(actual, "name 1");
    }

    @Test
    void mapsDescription() {
      assertDescription(actual, "description 1");
    }

    @Test
    void mapsUrl() {
      assertUrl(actual, "https://www.example.foo");
    }

    @Test
    void mapsContact() {
      assertContact(actual, "contact 1");
    }

    @Test
    void mapsOrganisation() {
      assertOrganisation(actual, "org 1");
    }

    @Test
    void mapsDocumentationUrl() {
      assertDocumentationUrl(actual, "https://www.exampledocs.foo");
    }

    private Api getApi() {
      return new Api(
          "name 1",
          "description 1",
          "https://www.example.foo",
          "contact 1",
          "org 1",
          "https://www.exampledocs.foo");
    }

    private void assertApiVersion(ApiMetadata apiMetadata, ApiMetadata.ApiVersion apiVersion) {
      assertThat(apiMetadata.getApiVersion()).isEqualTo(apiVersion);
    }

    private void assertName(ApiMetadata apiMetadata, String name) {
      assertThat(apiMetadata.getData().getName()).isEqualTo(name);
    }

    private void assertDescription(ApiMetadata apiMetadata, String description) {
      assertThat(apiMetadata.getData().getDescription()).isEqualTo(description);
    }

    private void assertUrl(ApiMetadata apiMetadata, String url) {
      assertThat(apiMetadata.getData().getUrl()).isEqualTo(URI.create(url));
    }

    private void assertContact(ApiMetadata apiMetadata, String contact) {
      assertThat(apiMetadata.getData().getContact()).isEqualTo(contact);
    }

    private void assertOrganisation(ApiMetadata apiMetadata, String organisation) {
      assertThat(apiMetadata.getData().getOrganisation()).isEqualTo(organisation);
    }

    private void assertDocumentationUrl(ApiMetadata apiMetadata, String documentationUrl) {
      assertThat(apiMetadata.getData().getDocumentationUrl())
          .isEqualTo(URI.create(documentationUrl));
    }
  }

  @Nested
  class FromApiMetadata {

    private Api actual;

    @BeforeEach
    void setup() {
      var apiMetadata = getApiMetadata();

      actual = mapper.from(apiMetadata);
    }

    private ApiMetadata getApiMetadata() {
      var metadata = new ApiMetadata();
      var data = new Data();
      data.setName("name 1");
      data.setDescription("description 1");
      data.setUrl(URI.create("https://www.example.foo"));
      data.setContact("contact 1");
      data.setOrganisation("org 1");
      data.setDocumentationUrl(URI.create("https://www.exampledocs.foo"));
      metadata.setData(data);
      return metadata;
    }

    @Test
    void mapsName() {
      assertName(actual, "name 1");
    }

    @Test
    void mapsDescription() {
      assertDescription(actual, "description 1");
    }

    @Test
    void mapsUrl() {
      assertUrl(actual, "https://www.example.foo");
    }

    @Test
    void mapsContact() {
      assertContact(actual, "contact 1");
    }

    @Test
    void mapsOrganisation() {
      assertOrganisation(actual, "org 1");
    }

    @Test
    void mapsDocumentationUrl() {
      assertDocumentationUrl(actual, "https://www.exampledocs.foo");
    }

    private void assertName(Api api, String name) {
      assertThat(api.name()).isEqualTo(name);
    }

    private void assertDescription(Api api, String description) {
      assertThat(api.description()).isEqualTo(description);
    }

    private void assertUrl(Api api, String url) {
      assertThat(api.url()).isEqualTo(url);
    }

    private void assertContact(Api api, String contact) {
      assertThat(api.contact()).isEqualTo(contact);
    }

    private void assertOrganisation(Api api, String organisation) {
      assertThat(api.organisation()).isEqualTo(organisation);
    }

    private void assertDocumentationUrl(Api api, String documentationUrl) {
      assertThat(api.documentationUrl()).isEqualTo(documentationUrl);
    }
  }
}
