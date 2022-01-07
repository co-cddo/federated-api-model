---
tags:
  - component
---
# Registry

The **registry** keeps record of known [[provider]] the [[fetcher]] should get information from. ^94979d

The registry should be able to manage source records with:

- Unique identifier ([[id]]).
- Source URL ([[url]])
- Organisation ([[ideas/schema/organisation]]).
- Schedule. Minimum/desired frequency of polling from the API.

### Paradata

- Last fetch ([[ideas/schema/timestamp]]). Track when the source was last queried.
- Last errors (?). We should keep track of the last n (e.g. 3) errors and use them to analyse whether we need to take action or not. E.g. 3 timeouts should be treated differently than 3 unresolved host.


## Unknowns

- [ ] Should the registry keep information of fetching frequency per record? This would address potential differences in SLAs across providers.
- [ ] What format should a schedule use?
- [ ] Should source record identifiers be the [[url]] to the [[provider]] API?
- [ ] Should the Organisation be normalised (i.e. have just the identifier) or inline the essential information for it for lineage purposes?