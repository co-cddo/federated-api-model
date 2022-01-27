package uk.gov.api.springboot.dtos;

public record MetadataDto(
    String apiVersion,
    String name,
    String description,
    String url,
    String contact,
    String organisation,
    String documentationUrl) {}
