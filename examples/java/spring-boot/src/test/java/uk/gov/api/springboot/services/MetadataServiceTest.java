package uk.gov.api.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.daos.MetadataDao;
import uk.gov.api.springboot.dtos.MetadataDto;
import uk.gov.api.springboot.repositories.MetadataRepository;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private MetadataService service;

    @Mock private MetadataRepository repository;

    @Test
    void returnsListOfMetadataDto() {
      when(repository.findAll()).thenReturn(List.of(getMetadataDao()));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasOnlyElementsOfType(MetadataDto.class);
    }

    @Test
    void mapsApiVersion() {
      var dao = getMetadataDao();
      when(repository.findAll()).thenReturn(List.of(dao));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(1);
      assertApiVersion(actual.get(0), "api.gov.uk/v1alpha");
    }

    @Test
    void mapsName() {
      var dao1 = getMetadataDao();
      dao1.setName("name 1");
      var dao2 = getMetadataDao();
      dao2.setName("name 2");
      var dao3 = getMetadataDao();
      dao3.setName("name 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertName(actual.get(0), "name 1");
      assertName(actual.get(1), "name 2");
      assertName(actual.get(2), "name 3");
    }

    @Test
    void mapsDescription() {
      var dao1 = getMetadataDao();
      dao1.setDescription("description 1");
      var dao2 = getMetadataDao();
      dao2.setDescription("description 2");
      var dao3 = getMetadataDao();
      dao3.setDescription("description 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertDescription(actual.get(0), "description 1");
      assertDescription(actual.get(1), "description 2");
      assertDescription(actual.get(2), "description 3");
    }

    @Test
    void mapsUrl() {
      var dao1 = getMetadataDao();
      dao1.setUrl("https://www.example.foo");
      var dao2 = getMetadataDao();
      dao2.setUrl("https://www.example.bar");
      dao2.setApiVersion("api.gov.uk/v1alpha");
      var dao3 = getMetadataDao();
      dao3.setUrl("https://www.example.baz");
      dao3.setApiVersion("api.gov.uk/v1alpha");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertUrl(actual.get(0), "https://www.example.foo");
      assertUrl(actual.get(1), "https://www.example.bar");
      assertUrl(actual.get(2), "https://www.example.baz");
    }

    @Test
    void mapsContact() {
      var dao1 = getMetadataDao();
      dao1.setContact("contact 1");
      var dao2 = getMetadataDao();
      dao2.setContact("contact 2");
      var dao3 = getMetadataDao();
      dao3.setContact("contact 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertContact(actual.get(0), "contact 1");
      assertContact(actual.get(1), "contact 2");
      assertContact(actual.get(2), "contact 3");
    }

    @Test
    void mapsOrganisation() {
      var dao1 = getMetadataDao();
      dao1.setOrganisation("org 1");
      var dao2 = getMetadataDao();
      dao2.setOrganisation("org 2");
      var dao3 = getMetadataDao();
      dao3.setOrganisation("org 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertOrganisation(actual.get(0), "org 1");
      assertOrganisation(actual.get(1), "org 2");
      assertOrganisation(actual.get(2), "org 3");
    }

    @Test
    void mapsDocumentationUrl() {
      var dao1 = getMetadataDao();
      dao1.setDocumentationUrl("https://www.exampledocs.foo");
      var dao2 = getMetadataDao();
      dao2.setDocumentationUrl("https://www.exampledocs.bar");
      var dao3 = getMetadataDao();
      dao3.setDocumentationUrl("https://www.exampledocs.baz");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<MetadataDto> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertDocumentationUrl(actual.get(0), "https://www.exampledocs.foo");
      assertDocumentationUrl(actual.get(1), "https://www.exampledocs.bar");
      assertDocumentationUrl(actual.get(2), "https://www.exampledocs.baz");
    }

    private MetadataDao getMetadataDao() {
      var dao = new MetadataDao();
      dao.setApiVersion("api.gov.uk/v1alpha");
      dao.setUrl("https://foo.bar");
      dao.setDocumentationUrl("https://foo.baz");
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
