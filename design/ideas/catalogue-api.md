---
---
# Catalogue API

The **catalogue api** is a _read-only_ HTTP API.

The content type SHOULD be `application/vnd.uk.gov.api.v1alpha+json` and accept `application/json` as a valid `Accept` value.


## Resources

- [[api-resource]]


## Api list

- endpoint:: `GET /apis/`
- headers::
  - `next`: Path the the next page if any.
  - `prev`: Path to the previous page if any.
  - `content-type`: `application/vnd.uk.gov.api.v1alpha+json`

Provides a paginated list of [[api-resource]] resources.


## Api

==NOTE==: The formal definition is fully defined as an [OpenAPI spec](https://github.com/co-cddo/federated-api-model/blob/main/schemas/v1alpha/openapi.yml) and a [JSON Schema](https://github.com/co-cddo/federated-api-model/blob/main/schemas/v1alpha/api-metadata.json). The information below is likely to be stale, take it with a pinch of salt.

- endpoint:: `GET /apis/{id}/`
- parameters::
  - `id`: A valid [[api-resource]] identifier.

Provides the full infoset for the requested record.

### Response

The response infoset MUST provide:

- `id` ([[ideas/schema/id]]): The record unique identifier.
- `name` ([[ideas/schema/text]]): The name for the API.
- `description` ([[ideas/schema/markdown]]): The description of what the API is about.
- `url` ([[ideas/schema/url]]): The  URL of the API.


### Errors

- `404`: If `id` is not found.
- `403`: If the request is not authorised. ==This should be handled by the [[gate]]==
- `401`: If the request is not authenticated. ==This should be handled by the [[gate]]==





## Unknowns

- [ ] Should the API expose the [[registry]] sources?
- [ ] Should the API expose organsiation information as top-level resource?
- [ ] Should the content be [[json-ld]] or ad-hoc design?
- [x] The name "record" is extremely generic. Should we have something like "api" or similar instead? "api" ==is already used in the OpenAPI spec==.