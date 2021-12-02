package uk.gov.api.springboot.services;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import models.metadata.ApiMetadata;
import models.metadata.Data;
import org.springframework.stereotype.Service;
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
      ApiMetadata apiMetadata = new ApiMetadata();
      apiMetadata.setApiVersion(ApiMetadata.ApiVersion.fromValue(dao.getApiVersion()));
      Data data = new Data();
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
