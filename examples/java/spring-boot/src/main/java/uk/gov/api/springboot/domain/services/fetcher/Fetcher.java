package uk.gov.api.springboot.domain.services.fetcher;

import java.util.List;
import uk.gov.api.springboot.domain.model.Api;

/**
 * A core domain object for retrieving API metadata objects from some external source.
 *
 * <p>This could be using the HTTP contract, or could rely on other means such as a file-based
 * parsing.
 */
public interface Fetcher {
  /**
   * Fetch the {@link Api}s that a given Provider is aware of.
   *
   * @param baseUrl the root of the service's URL. Should not include i.e. <code>/apis</code> or
   *     other paths that are version-specific.
   * @return the {@link Api}s found
   * @throws VersionNotSupportedException if the version of the contract this {@link Fetcher}
   *     supports is not supported by the Provider
   * @throws ClientErrorException if there was an indication that this client performed an invalid
   *     request, for instance mapping to an HTTP 400
   * @throws TemporaryErrorException
   */
  List<Api> fetch(String baseUrl)
      throws VersionNotSupportedException, ClientErrorException, TemporaryErrorException;

  /**
   * The version of the contract that is being to communicate is not supported.
   *
   * <p>Most likely mapping directly to an HTTP 406.
   *
   * @see ClientErrorException
   */
  class VersionNotSupportedException extends Exception {}

  /**
   * There was an indication that this client performed an invalid request.
   *
   * <p>Most likely mapping directly to an HTTP 4xx, but not an HTTP 406.
   *
   * <p>This is an indication that either this client is malformed, or the remote is malformed, and
   * one/both side has misimplemented the contract for this version.
   *
   * @see VersionNotSupportedException
   */
  class ClientErrorException extends Exception {}

  /**
   * There was an indication that the provider had an error during the request.
   *
   * <p>Most likely mapping directly to an HTTP 5xx.
   */
  class TemporaryErrorException extends Exception {}
}
