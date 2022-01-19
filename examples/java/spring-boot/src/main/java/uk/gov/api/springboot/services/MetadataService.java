package uk.gov.api.springboot.services;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import uk.gov.api.models.metadata.v1alpha.ApiMetadata;
import uk.gov.api.models.metadata.v1alpha.Data;
import uk.gov.api.springboot.daos.MetadataDao;
import uk.gov.api.springboot.repositories.MetadataRepository;

@Service
public class MetadataService {

  private final MetadataRepository repository;

  public MetadataService(MetadataRepository repository) {
    this.repository = repository;
  }

  public List<ApiMetadata> retrieveAll() {
    return repository.findAll().stream().map(convertToApiMetadata()).toList();
  }

  private static Function<MetadataDao, ApiMetadata> convertToApiMetadata() {
    return dao -> {
      var apiMetadata = new ApiMetadata();
      apiMetadata.setApiVersion(ApiMetadata.ApiVersion.fromValue(dao.getApiVersion()));
      var data = new Data();
      data.setName(dao.getName());
      data.setDescription(dao.getDescription());
      data.setUrl(URI.create(dao.getUrl()));
      data.setContact(dao.getContact());
      data.setOrganisation(dao.getOrganisation());
      data.setDocumentationUrl(URI.create(dao.getDocumentationUrl()));
      apiMetadata.setData(data);
      return apiMetadata;
    };
  }
}
