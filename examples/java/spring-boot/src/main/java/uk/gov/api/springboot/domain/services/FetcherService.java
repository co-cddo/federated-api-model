package uk.gov.api.springboot.domain.services;

import java.util.List;
import uk.gov.api.springboot.domain.model.Api;
import uk.gov.api.springboot.domain.model.Registry;

/** Interface representing a fetcher service, which fetches lists of APIs from external services. */
public interface FetcherService {

  List<Api> fetch(Registry.Entry entry);
}
