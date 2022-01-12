---
tags:
  - schema
  - type
---
# URL

A **url value** MUST be a valid _publicly resolvable_ HTTP address conforming to the [WHATWG specification]. A url value MUST also be absolute and fully expanded.

There are a few competing specifications to account for:

- [WHATWG specification]. It is managed by the major browser vendors and claim to make an effort to align with [IETF RFC 3986] (URI) and [IETF RFC 3987] (IRI).
- [IETF RFC 3986]. Uniform Resource Identifier (URI).
- [IETF RFC 3987]. Internationalized Resource Identifiers (IRIs)

Good article to read on the topic: [Don’t mix url parsers](https://daniel.haxx.se/blog/2022/01/10/dont-mix-url-parsers/).

Given that the WHATWG specification claims to make an active effort to be compatible with the other two specs and is what the major web clients align with, it should be safer to use this specification instead of any of the other possibilities. Any change on the power dynamics or new moves from IETF should be considered to reassess this choice.

That said, [[json-schema]] depends on [IETF RFC 3986] for its internal URI resolution and both [IETF RFC 3986] and [IETF RFC 3987] for the [resource identifiers](http://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.7.3.5) in their validation specification.

[open-api] depends on [IETF RFC 3986] for [URI and URL resolution](https://spec.openapis.org/oas/latest.html#relative-references-in-uris) with no mention of IRIs but claiming to align with [json-schema].

Finally, GOV.UK mentions [IETF RFC 3986] in [Persistent Resolvable Identifiers](https://www.gov.uk/government/publications/open-standards-for-government/persistent-resolvable-identifiers) (it hasn't been reviewed since 2017).


[WHATWG specification]: https://url.spec.whatwg.org/
[IETF FC 3986]: https://datatracker.ietf.org/doc/html/rfc3986/
[IETF FC 3987]: https://datatracker.ietf.org/doc/html/rfc3987/