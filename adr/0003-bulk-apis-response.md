---
creation_date: 2021-11-26
decision_date: 2021-12-01
status: accepted
---
# Introduce a Bulk API Response model

## Context

When interacting with a service that produces a list of known APIs, we need to be able to list all APIs that are known about, and we have permission to view.

## Decision

![Entity Relationship Diagram for API Metadata objects](https://kroki.io/erd/svg/eNqtkbEOQiEMRXe-omE0MrzVTePq4vriUKF5IfIAC7gY_11wIi44uDW5J6fN7Xwo7raP9kwpBp_oIiRGqx7EyQYv4QkOr-R2IFNm65ctMN2LZTISXsJgxg7pIzFX64kyNuaP1uNH53GlkYWSZhtzXTggC7sBoYPPqPOACrygtwl_WClN0GWlam2wqheMShFfj4JJqQ10HYtubuEErao331ukOw==)

<details>

<summary>Entity Relationship Diagram (raw)</summary>

```erd
[BulkApiResponse]
"api-version" { label: "string, required" }
data { label: "required" }

[ApiMetadata]
"api-version" { label: "string, required" }
data { label: "required" }

[Data]
name { label: "string, required" }
description { label: "string, required" }
url { label: "string, required" }
contact { label: "string, required" }
organisation { label: "string, required" }
"documentation-url" { label: "string, required" }

BulkApiResponse 1--* ApiMetadata
ApiMetadata 1--1 Data
```

</details>

API Versioning is introduced as well to ensure that the overall response is versioned, as well as each metadata object.

## Consequences

N/A

## See also

- https://github.com/co-cddo/federated-api-model/pull/5
- https://github.com/co-cddo/federated-api-model/pull/12
