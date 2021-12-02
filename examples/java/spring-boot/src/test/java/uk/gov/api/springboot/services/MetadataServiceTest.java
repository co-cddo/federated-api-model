package uk.gov.api.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import models.metadata.ApiMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.api.springboot.daos.MetadataDao;
import uk.gov.api.springboot.repositories.MetadataRepository;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private MetadataService service;

    @Mock private MetadataRepository repository;

    @Test
    void returnsListOfApiMetadata() {
      when(repository.findAll()).thenReturn(List.of(getMetadataDao()));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasOnlyElementsOfType(ApiMetadata.class);
    }

    @Test
    void mapsApiVersion() {
      MetadataDao dao = getMetadataDao();
      when(repository.findAll()).thenReturn(List.of(dao));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(1);
      assertApiVersion(actual.get(0), ApiMetadata.ApiVersion.API_GOV_UK_V_1_ALPHA);
    }

    @Test
    void mapsName() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setName("name 1");
      MetadataDao dao2 = getMetadataDao();
      dao2.setName("name 2");
      MetadataDao dao3 = getMetadataDao();
      dao3.setName("name 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertName(actual.get(0), "name 1");
      assertName(actual.get(1), "name 2");
      assertName(actual.get(2), "name 3");
    }

    @Test
    void mapsDescription() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setDescription("description 1");
      MetadataDao dao2 = getMetadataDao();
      dao2.setDescription("description 2");
      MetadataDao dao3 = getMetadataDao();
      dao3.setDescription("description 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertDescription(actual.get(0), "description 1");
      assertDescription(actual.get(1), "description 2");
      assertDescription(actual.get(2), "description 3");
    }

    @Test
    void mapsUrl() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setUrl("https://www.example.foo");
      MetadataDao dao2 = getMetadataDao();
      dao2.setUrl("https://www.example.bar");
      dao2.setApiVersion("api.gov.uk/v1alpha");
      MetadataDao dao3 = getMetadataDao();
      dao3.setUrl("https://www.example.baz");
      dao3.setApiVersion("api.gov.uk/v1alpha");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertUrl(actual.get(0), "https://www.example.foo");
      assertUrl(actual.get(1), "https://www.example.bar");
      assertUrl(actual.get(2), "https://www.example.baz");
    }

    @Test
    void mapsContact() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setContact("contact 1");
      MetadataDao dao2 = getMetadataDao();
      dao2.setContact("contact 2");
      MetadataDao dao3 = getMetadataDao();
      dao3.setContact("contact 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertContact(actual.get(0), "contact 1");
      assertContact(actual.get(1), "contact 2");
      assertContact(actual.get(2), "contact 3");
    }

    @Test
    void mapsOrganisation() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setOrganisation("org 1");
      MetadataDao dao2 = getMetadataDao();
      dao2.setOrganisation("org 2");
      MetadataDao dao3 = getMetadataDao();
      dao3.setOrganisation("org 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertOrganisation(actual.get(0), "org 1");
      assertOrganisation(actual.get(1), "org 2");
      assertOrganisation(actual.get(2), "org 3");
    }

    @Test
    void mapsDocumentationUrl() {
      MetadataDao dao1 = getMetadataDao();
      dao1.setDocumentationUrl("https://www.exampledocs.foo");
      MetadataDao dao2 = getMetadataDao();
      dao2.setDocumentationUrl("https://www.exampledocs.bar");
      MetadataDao dao3 = getMetadataDao();
      dao3.setDocumentationUrl("https://www.exampledocs.baz");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertDocumentationUrl(actual.get(0), "https://www.exampledocs.foo");
      assertDocumentationUrl(actual.get(1), "https://www.exampledocs.bar");
      assertDocumentationUrl(actual.get(2), "https://www.exampledocs.baz");
    }

    private MetadataDao getMetadataDao() {
      MetadataDao dao = new MetadataDao();
      dao.setApiVersion("api.gov.uk/v1alpha");
      dao.setUrl("https://foo.bar");
      dao.setDocumentationUrl("https://foo.baz");
      return dao;
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
}
