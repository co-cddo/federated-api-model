package uk.gov.api.springboot.domain.model;

import java.util.List;

/**
 * Interface representing a registry, which returns a list of entries representing external services
 * to parse.
 */
public class Registry {

  public record Entry(java.util.UUID id, String baseUrl) {}

  public interface Storage {
    List<Entry> retrieveAll();
  }

  private final Storage storage;

  public Registry(Storage storage) {
    this.storage = storage;
  }

  public List<Entry> retrieveAll() {
    return storage.retrieveAll();
  }
}
