---
creation_date: 2021-12-01
decision_date: 2021-12-02
status: accepted
---
# Utilise JSON Schema for metadata model

## Context

Up until this point, we've been creating Plain Old Java Objects (POJOs) to manage the structure of our metadata objects, and to manage the (de)serialisation.

This does not lend itself well to working across different tech stacks, as it only allows for a Java application that uses Jackson for their JSON layer.

## Decision

We have decided to migrate from POJOs to using a JSON Schema to manage the model.

Instead of using a POJO that mirrors the format of the JSON Schema, we will utilise tools to automagically generate the Java classes from that schema.

## Consequences

- Using a tool to build these classes leads to code that hasn't been code reviewed being part of our production codebase
- Using a tool to build these classes leads to extra build time to generate them
- New versions of JSON Schema may not be supported by the tools in use, so we may need to arbitrarily limit the JSON Schema version we use, or upstream changes to implement support before it's usable
- We are now not tied to a specific tech stack for the models' usage
- Models are now produced design-first, rather than implementation-first
