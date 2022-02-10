/**
 * The Application (Service) Ring is for only application-specific configuration, such as Spring
 * Boot, or Spring Dependency Injection, as well as any logic that is implementation-specific
 * plumbing that makes use of the Domain layer through delegation to an {@link
 * org.jmolecules.architecture.onion.classical.DomainServiceRing} service and by using {@link
 * org.jmolecules.architecture.onion.classical.DomainModelRing} models.
 */
@ApplicationServiceRing
package uk.gov.api.springboot.application;

import org.jmolecules.architecture.onion.classical.ApplicationServiceRing;
