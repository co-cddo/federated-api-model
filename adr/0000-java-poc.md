---
creation_date: 2021-11-23
decision_date: 2021-11-23
status: accepted
---
# Use Java and Spring Boot as the basis for the Proof of Concept

## Context

As part of setting up a Proof of Concept for understanding how the metadata model could work, we need to decide on a tech stack.

## Decision

As both Bethan and Jamie have used Java extensively before, as well as Java being a supported [GDS Way Language](https://gds-way.cloudapps.digital/standards/programming-languages.html), we will use Java.

Java is commonly used across the government and has good library support for building Web APIs.

We will use Spring Boot as there is a strong level of experience in the team, although it may not be widely deployed in GDS.

## Consequences

Spring Boot is not widely used, so maintenance longer term may be more difficult. However, as this is a POC, the expectation is that it may not be supported once the discovery phase is complete.

Spring Boot will provide us the fastest velocity for delivery, allowing us to exercise good development practices and build on top of existing knowledge.
