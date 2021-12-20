---
creation_date: 2021-11-24
decision_date: 2021-11-25
status: accepted
---
# Initial Metadata Model - `v1alpha`

## Context

We need a minimal set of data that is required to reason about an API.

## Decision

The minimal set of data required is the:

- `name`
- `description`
- `url`
- `contact`
- `organisation`
- `documentationUrl`

![Entity Relationship Diagram for API Metadata objects](https://kroki.io/erd/svg/eNqNkDEOAiEQRXtOMaGWYls7E1tPsLEYYbKZhAUcZm2Mdxes6LCeN-___PVS-EaKARXvxmJh9yKpnJOFN0R8UDyDrSqcthMIPQ8WChY-pn8MyHgy6_WnS7jTzELVCxdtgRPykDghfE6KXidUlg0TV_wj0obsj52atcOuNZiNYoY5YXFugb7EF9NYelc=)

<details>

<summary>Entity Relationship Diagram (raw)</summary>

```erd
[ApiMetadata]
api-version { label: "string, required" }
data { label: "required" }

[Data]
name { label: "string, required" }
description { label: "string, required" }
url { label: "string, required" }
contact { label: "string, required" }
organisation { label: "string, required" }
documentationUrl { label: "string, required" }

ApiMetadata 1--1 Data
```

</details>

## Consequences

As an `api-version` is present in each `ApiMetadata` object, we are making it clear to consumers of the API that API versioning is a required property to consider and handle versioning on their side.

At this point, `api-version` is a free text field.

`api-version` has been introduced as a parameter in the objects themselves, rather than i.e. a header or a part of the Media Type of the response, so we can support file-based ingestion.

## See also

- https://github.com/co-cddo/federated-api-model/pull/2
