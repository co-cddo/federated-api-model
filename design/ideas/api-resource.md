---
tags:
  - resource
---
# API

The API is the main resource the federated model is concerned about.

- Unique identifier ([[id]])
- Name ([[text]])
- Description ([[markdown]])
- URL ([[url]])
- Maintainer ([[organisation-resource]])
- Contact ([[email]])


The [[catalogue-api]] describes the API resource in terms of a RESTful API using [[json]] but the federated model can be expressed using other models such as [[graphql]] schema, [[protobuffs]] or [[rdf]].


## GraphQL

```graphql
scalar Email @specifiedBy(url: "https://datatracker.ietf.org/doc/html/rfc5322")
scalar Uuid @specifiedBy(url: "https://datatracker.ietf.org/doc/html/rfc4122")
scalar Url @specifiedBy(url: "https://datatracker.ietf.org/doc/html/rfc3986")

type Api {
  id: Uuid!
  name: String! @nonempty
  description: String! @markdown
  url: Url!
  maintainer: Organisation!
  contact: Email!
}
```

Notice all fields are required which, in theory will impose challenges when considering agressive changes and backwards compatibility. On the flip side, not ensuring these core pieces of information would render the record useless.

Instead of a concrete `type` it could be expressed as an interface which could help express the different metadata purposes. In practice it should be irrelevant for the user.

Whether to use a scalar like `scalar Url` or a directive `@url` is unclear to me. It might boil down to what's more ergonomic to implement with the tools of choice.


## Unknowns

- [ ] Although the "maintainer" has a point of contact, each API maintained by the same organisation could have different contacts. Should be considered whether that should be actually expressed as independent [[organisation-resource]]. Potential problems would be conflating the need for a point of contact (this case) with the need for grouping by ownership and sourcing (see [[registry]]).
- [ ] GraphQL defines the [scalar String type](https://spec.graphql.org/October2021/#sec-String) as a sequence of Unicode code points but does not constrain to [[utf-8]]. If GraphQL is to be implemented, [[utf-8]] should be declared as a constraint explicitly by the federated model specification to ensure compatibility across implementations, clients, etc.