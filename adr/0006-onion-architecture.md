---
creation_date: 2022-01-28
decision_date: 2022-02-08
status: accepted
---
# Use of Onion Architecture in Spring Boot

## Context

As a means to better structure the project, enforcing consistency of our underlying data structures and project's implementation, we want to enforce a common code architecture.

[jMolecules](https://github.com/xmolecules/jmolecules) is a Java library that can do this for us, both with rules, and a common language for describing the architecture, and currently supports:

- Layered Architecture
- Onion Architecture
- Domain Driven Design (DDD)

Using this, and [ArchUnit](https://www.archunit.org/) rules, we can enforce the right architecture style of our application.

## Decision

We have decided to migrate to Onion Architecture, as the most straightforward of the options to get started with.

## Consequences

- We are more considered with the approach of our software architecture, leading to pure Domain objects and logic being separated from the Spring Boot implementation.
- This leads us to being more easily able to split the domain logic out into a separate module/library, allowing for others to consume the core business rules without requiring the use of the Spring Boot application
- This will provide a little increase in overhead of development while we get used to developing in this style, slowing us down a little
- It may also introduce overhead for people who are coming to read this as a reference implementation.

## See also

- https://herbertograca.com/2017/09/21/onion-architecture/
- https://herbertograca.com/2017/08/03/layered-architecture/ (in particular the anti-pattern which we encountered when trialing it)
