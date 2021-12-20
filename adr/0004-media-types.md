---
creation_date: 2021-11-25
decision_date: 2021-11-26
status: accepted
---
# Use of vendored media types for content-type negotiation of API versioning

## Context

As well as introducing API versioning within our data objects, which supports ingestion or parsing of the objects themselves, we should also allow for versioning our transport layer, which is currently HTTP.

## Decision

We will produce/consume custom media types using the vendor tree for explicit versioning of HTTP interactions.

For example, when using the `v1alpha` schema, the media type is `application/vnd.uk.gov.api.v1alpha+json`.

The use of the `+json` suffix allows well-formed clients to fall back to using JSON decoding and parsing for the response.

## Consequences

This may lead to extra work in tooling to produce/consume the custom media types for HTTP interactions.

This is a commonly used pattern, therefore developers are likely to have at least some understanding about it.

## See also

- https://en.wikipedia.org/wiki/Media_type#Vendor_tree
