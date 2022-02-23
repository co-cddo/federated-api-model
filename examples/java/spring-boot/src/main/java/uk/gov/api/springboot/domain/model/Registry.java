package uk.gov.api.springboot.domain.model;

import java.util.List;

/**
 * Interface representing a registry, which returns a list of entries representing external services
 * to parse.
 */
public interface Registry {

  List<Entry> retrieveAll();

  record Entry(String id, String baseUrl) {}
}
