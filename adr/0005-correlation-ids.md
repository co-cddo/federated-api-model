---
creation_date: 2022-02-01
decision_date: 2022-02-01
status: accepted
---
# Use of optional Correlation IDs for tracking requests in logs

## Context

Correlation IDs (sometimes also known as "tracking IDs" or "tracing IDs") allow us to track a request across a whole interaction with our service. If there are problems we need to debug, having a unique identifier per transaction that we can expose to our logging and monitoring will make it easier to track down the issue.

The ID will also be included in the response header, so that consumers will also have a view of it, if they had not provided one in the request.

Leaving it as optional makes it easier for us to test our API in the browser or with `curl`, but we may want to make it required in the future.

## Decision

We will include an optional correlation ID in the request and response headers in our contract.

The format of the ID will be a Universally Unique Identifier (UUID), and we will require version 4. We favour UUIDv4 because this is a commonly used version with low chances of collision, and consumers can easily generate their own without worrying about uniqueness.

## Consequences

The decision to make the correlation ID optional means that we may not always get the benefits of tracking requests in our logs if some consumers choose not to use it.
