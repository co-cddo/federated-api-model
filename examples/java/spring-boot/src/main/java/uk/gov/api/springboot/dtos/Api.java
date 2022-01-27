package uk.gov.api.springboot.dtos;

/** Domain object for API (metadata) objects. */
public record Api(
    String apiVersion,
    String name,
    String description,
    String url,
    String contact,
    String organisation,
    String documentationUrl) {}
