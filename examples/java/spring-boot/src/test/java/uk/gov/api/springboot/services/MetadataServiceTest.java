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
import uk.gov.api.springboot.models.metadata.ApiMetadata;
import uk.gov.api.springboot.repositories.MetadataRepository;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

  @Nested
  class RetrieveAll {

    @InjectMocks private MetadataService service;

    @Mock private MetadataRepository repository;

    @Test
    void returnsListOfApiMetadata(@Mock MetadataDao dao) {
      when(repository.findAll()).thenReturn(List.of(dao));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasOnlyElementsOfType(ApiMetadata.class);
    }

    @Test
    void mapsApiVersion() {
      MetadataDao dao1 = new MetadataDao();
      dao1.setApiVersion("version 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setApiVersion("version 2");
      MetadataDao dao3 = new MetadataDao();
      dao3.setApiVersion("version 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertApiVersion(actual.get(0), "version 1");
      assertApiVersion(actual.get(1), "version 2");
      assertApiVersion(actual.get(2), "version 3");
    }

    @Test
    void mapsName() {
      MetadataDao dao1 = new MetadataDao();
      dao1.setName("name 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setName("name 2");
      MetadataDao dao3 = new MetadataDao();
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
      MetadataDao dao1 = new MetadataDao();
      dao1.setDescription("description 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setDescription("description 2");
      MetadataDao dao3 = new MetadataDao();
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
      MetadataDao dao1 = new MetadataDao();
      dao1.setUrl("url 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setUrl("url 2");
      MetadataDao dao3 = new MetadataDao();
      dao3.setUrl("url 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertUrl(actual.get(0), "url 1");
      assertUrl(actual.get(1), "url 2");
      assertUrl(actual.get(2), "url 3");
    }

    @Test
    void mapsContact() {
      MetadataDao dao1 = new MetadataDao();
      dao1.setContact("contact 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setContact("contact 2");
      MetadataDao dao3 = new MetadataDao();
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
      MetadataDao dao1 = new MetadataDao();
      dao1.setOrganisation("org 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setOrganisation("org 2");
      MetadataDao dao3 = new MetadataDao();
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
      MetadataDao dao1 = new MetadataDao();
      dao1.setDocumentationUrl("doc url 1");
      MetadataDao dao2 = new MetadataDao();
      dao2.setDocumentationUrl("doc url 2");
      MetadataDao dao3 = new MetadataDao();
      dao3.setDocumentationUrl("doc url 3");
      when(repository.findAll()).thenReturn(List.of(dao1, dao2, dao3));

      List<ApiMetadata> actual = service.retrieveAll();

      assertThat(actual).hasSize(3);
      assertDocumentationUrl(actual.get(0), "doc url 1");
      assertDocumentationUrl(actual.get(1), "doc url 2");
      assertDocumentationUrl(actual.get(2), "doc url 3");
    }

    private void assertApiVersion(ApiMetadata apiMetadata, String apiVersion) {
      assertThat(apiMetadata.getApiVersion()).isEqualTo(apiVersion);
    }

    private void assertName(ApiMetadata apiMetadata, String name) {
      assertThat(apiMetadata.getData().getName()).isEqualTo(name);
    }

    private void assertDescription(ApiMetadata apiMetadata, String description) {
      assertThat(apiMetadata.getData().getDescription()).isEqualTo(description);
    }

    private void assertUrl(ApiMetadata apiMetadata, String url) {
      assertThat(apiMetadata.getData().getUrl()).isEqualTo(url);
    }

    private void assertContact(ApiMetadata apiMetadata, String contact) {
      assertThat(apiMetadata.getData().getContact()).isEqualTo(contact);
    }

    private void assertOrganisation(ApiMetadata apiMetadata, String organisation) {
      assertThat(apiMetadata.getData().getOrganisation()).isEqualTo(organisation);
    }

    private void assertDocumentationUrl(ApiMetadata apiMetadata, String documentationUrl) {
      assertThat(apiMetadata.getData().getDocumentationUrl()).isEqualTo(documentationUrl);
    }
  }
}
