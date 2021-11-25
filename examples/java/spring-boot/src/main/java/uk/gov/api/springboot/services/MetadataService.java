package uk.gov.api.springboot.services;

import java.util.List;
import java.util.function.Function;
import uk.gov.api.springboot.daos.MetadataDao;
import uk.gov.api.springboot.models.metadata.ApiMetadata;
import uk.gov.api.springboot.models.metadata.Data;
import uk.gov.api.springboot.repositories.MetadataRepository;

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
      apiMetadata.setApiVersion(dao.getApiVersion());
      Data data = new Data();
      data.setName(dao.getName());
      data.setDescription(dao.getDescription());
      data.setUrl(dao.getUrl());
      data.setContact(dao.getContact());
      data.setOrganisation(dao.getOrganisation());
      data.setDocumentationUrl(dao.getDocumentationUrl());
      apiMetadata.setData(data);
      return apiMetadata;
    };
  }
}
