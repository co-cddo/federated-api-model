---
---
# Catalogue API

The **catalogue api** is a _read-only_ HTTP API.

The content type SHOULD be `application/json`.


## Resources

- [[record]]


## Record list

- endpoint:: `GET /records/`
- headers::
  - `next`: Path the the next page if any.
  - `prev`: Path to the previous page if any.
  - `content-type`

Provides a paginated list of records.


## Record

- endpoint:: `GET /records/{id}/`
- parameters::
  - `id`: A valid record identifier.

Provides the full infoset for the requested record.

### Response

The response infoset MUST provide:

- `id (ID)`: The record unique identifier.
- `name (Text)`: The name for the API.
- `description (Markdown)`: The description of what the API is about.

### Errors

- `404`: If `id` is not found.
- `403`: If the request is not authorised. ==This should be handled by the [[gate]]==
- `401`: If the request is not authenticated. ==This should be handled by the [[gate]]==





## Unknowns

- [ ] Should the API expose the [[registry]] sources?
- [ ] Should the API expose organsiation information as top-level resource?
- [ ] Should the content be [[json-ld]] or ad-hoc design?
- [ ] The name "record" is extremely generic. Should we have something like "api" or similar instead?