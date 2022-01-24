package uk.gov.api.springboot.dtos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.gov.api.springboot.daos.MetadataDao;

class MetadataDtoTest {
  @Nested
  class Of {

    @Test
    void mapsApiVersion() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertApiVersion(actual, "api.gov.uk/v1alpha");
    }

    @Test
    void mapsName() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertName(actual, "name 1");
    }

    @Test
    void mapsDescription() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertDescription(actual, "description 1");
    }

    @Test
    void mapsUrl() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertUrl(actual, "https://www.example.foo");
    }

    @Test
    void mapsContact() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertContact(actual, "contact 1");
    }

    @Test
    void mapsOrganisation() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertOrganisation(actual, "org 1");
    }

    @Test
    void mapsDocumentationUrl() {
      var dao = getMetadataDao();

      MetadataDto actual = MetadataDto.of(dao);

      assertDocumentationUrl(actual, "https://www.exampledocs.foo");
    }

    private MetadataDao getMetadataDao() {
      var dao = new MetadataDao();
      dao.setName("name 1");
      dao.setDescription("description 1");
      dao.setContact("contact 1");
      dao.setOrganisation("org 1");
      dao.setDocumentationUrl("https://www.exampledocs.foo");
      dao.setApiVersion("api.gov.uk/v1alpha");
      dao.setUrl("https://www.example.foo");
      return dao;
    }

    private void assertApiVersion(MetadataDto metadataDto, String apiVersion) {
      assertThat(metadataDto.apiVersion()).isEqualTo(apiVersion);
    }

    private void assertName(MetadataDto metadataDto, String name) {
      assertThat(metadataDto.name()).isEqualTo(name);
    }

    private void assertDescription(MetadataDto metadataDto, String description) {
      assertThat(metadataDto.description()).isEqualTo(description);
    }

    private void assertUrl(MetadataDto metadataDto, String url) {
      assertThat(metadataDto.url()).isEqualTo(url);
    }

    private void assertContact(MetadataDto metadataDto, String contact) {
      assertThat(metadataDto.contact()).isEqualTo(contact);
    }

    private void assertOrganisation(MetadataDto metadataDto, String organisation) {
      assertThat(metadataDto.organisation()).isEqualTo(organisation);
    }

    private void assertDocumentationUrl(MetadataDto metadataDto, String documentationUrl) {
      assertThat(metadataDto.documentationUrl()).isEqualTo(documentationUrl);
    }
  }
}
