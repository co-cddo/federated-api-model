package uk.gov.api.springboot.domain.model;

/** Domain object for API (metadata) objects. */
public record Api(
    String name,
    String description,
    String url,
    String contact,
    String organisation,
    String documentationUrl) {}
