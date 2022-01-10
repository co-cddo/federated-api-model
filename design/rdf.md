---
---
# Resource Description Framework (RDF)

[RDF] is a directed-labelled graph data model originally designed to accomodate for "the web of data".

The nature of the model makes it low-cost to merge different bits of data coming from different unrelated places.

The cost though is that the system managing the graph (or graphs) must know the set of vocabularies and ontologies the data relies on. This tends to bring friction to human consumers if not dealt properly.

## Considerations

- RDF is not popular enough in contrast with other models (e.g. [[graphql]], relational model) even though it has been around for decades. It is growing again though and has strong adopters in government including the Scottish government and ONS to name a few.
- RDF is a model that assumes no constraints on data so multiple triples (assertions) can be brought together with no way to validate whether it conforms to a known shape. Recently [SHACL] has been getting more popular as a way to define shapes and constraints so data can be verified against known expectations.
- RDF tends to rely on [XML Schema datatypes] although theoretically not constrained by them. When choosing RDF (e.g. [[json-ld]], [[turtle]]) be careful on whether the assumptions hold when translating from/to [[json-schema]], [[open-api]], [[protobuffs]], etc. For example:
  - Strings and allowed range of encodings.
  - Date and time modelling (although based on [[iso8601]] it has a unique datatype hierarchy to watch out for).
  - Regular expressions _might_ not be compatible, at least partially.
  - Numbers can be of different types which is not the case in [[json]] for example.
  - URI, IRI, CURIE, [[url]], URN, blank nodes and identifiers must be understood properly.


[RDF]: https://www.w3.org/RDF/
[SHACL]: https://www.w3.org/TR/shacl/
[XML Schema datatypes]: https://www.w3.org/TR/xmlschema-2/