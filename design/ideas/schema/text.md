---
tags:
  - schema
  - type
---
# Text

A **text value** MUST be a valid [[utf-8]] string.

When a value is treated as _text_ it won't be parsed further besides basic UTF-8 checking. So, if the value has marks like the ones you could expect in [[markdown]] they should be kept verbatim.


## Unknowns

- [ ] Should a text value be sanitised to a UTF-8 subset? E.g. removing NULL (`\u0000`) and other control characters like direction marks?