---
name: issue-flow
description: Use when triaging, labeling, or moving any miteve-apk issue through backlog, ready, in-progress, review, and done states.
license: MIT
compatibility: opencode
---

# Issue Flow — triage and state only

This skill covers ONLY labels, states, and movement rules. Bodies live in `issue-bug`, `issue-feature`, `issue-task`.

## Rules

- Labels (required on triage): `form:phone`, `form:tv`, or `form:both` + `area:<feature>` (e.g. `area:player`) + one of `type:bug|type:feature|type:task`. Secrets-adjacent issues get `area:signing` and never contain key material.
- States: `backlog → ready (has acceptance + form label) → in-progress (branch linked) → review (PR linked) → done (acceptance checked + screenshots mobile+TV where UI)`.
- `ready` requires: form label set, acceptance criteria present, api impact stated (`new-api endpoint or none`). Bugs require reproducible steps + form factor.
- Close rule: UI issues close only with both mobile and TV evidence attached (or single-form justification from `parity-phone-tv`).

## Out of scope

- Do NOT define issue bodies here. See `issue-bug`, `issue-feature`, `issue-task`.
- Do NOT define PR body here. See `pr-description`.
- Do NOT define QA execution here. See `apply-qa-apk`.
