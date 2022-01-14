---
tags:
  - schema
  - type
---
# Id

An **id value** MUST be ==TODO==


An identifier should have a few characteristics:

- **Resilient to organisational changes**
  - For example, if an API is owned by ALB X and after a while the ownership moves to its parent Department A, the identifier should not be changed.
  - This is typically a strong reason for not using [[url]]s as identifiers as they tend to reflect not only owner (e.g. domain) but also internal structure (e.g. subdomain, path). This clashes with the [[rdf]] model but there are workarounds that could be considered, probably paying with some more management complexity.
- **Unique**
  - And straightforward to verify its uniqueness.
  - I personally favour [UUIDv4] but it imposes friction as it's difficult to memorise. It has the advantage of having affordable low chances of collision which means that independent agents can mint their own without worrying about uniqueness.
- **Non-guessable**
  - This mainly applies to public identifiers and is mainly about avoiding autoincremental indexes that could be used as a vector of attack against the catalogue.
- **Resilient to information changes**
  - This is a tradeoff to keep in mind. When an information change, changes the identity, then the identifier should be linked to that. E.g. git uses hashes to identify blobs of content. Otherwise, identity should be independent from identifier which is to say that the identifier should be preserved across information changes. E.g. an existing API changes it's point of contact.



## Unknowns

- [ ] We need to restrict what are acceptable identifiers in this model.
- [ ] Do we have different kinds of identifiers? opaque and not?
- [ ] For [[api-resource]] resources, do we accept [[url]] as identifiers?



[UUIDv4]: https://en.wikipedia.org/wiki/Universally_unique_identifier#Version_4_(random)