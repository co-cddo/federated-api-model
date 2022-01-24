package uk.gov.api.springboot.dtos;

import uk.gov.api.springboot.daos.MetadataDao;

public record MetadataDto(
    String apiVersion,
    String name,
    String description,
    String url,
    String contact,
    String organisation,
    String documentationUrl) {
  public static MetadataDto of(MetadataDao dao) {
    return new MetadataDto(
        dao.getApiVersion(),
        dao.getName(),
        dao.getDescription(),
        dao.getUrl(),
        dao.getContact(),
        dao.getOrganisation(),
        dao.getDocumentationUrl());
  }
}
