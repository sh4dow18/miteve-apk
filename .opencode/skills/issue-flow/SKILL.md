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
- On create (mandatory): set `assignees: [<creator-login>]` (whoever creates the issue) + labels by kind: feature → `type:feature`, bug → `type:bug`, task → `type:task`, always with `form:phone|tv|both` + `area:<feature>`. If a label does not exist in the repo, create it first, then apply it.
- Type + Start date (mandatory): set issue field Type = kind (`Feature`|`Bug`|`Task`, via `type` on create/update — the project has no Type field) and ProjectV2 `Miteve APK` field `Start date` = creation date (`YYYY-MM-DD`, today) via `updateProjectV2ItemFieldValue`.
- Title format (mandatory): `emoji + nombre` — `✨` Feature, `🐛` Bug, `📋` Task (e.g. `✨ Create Miteve Native App`). Always prefix on create; fix on triage if missing.
- Language (mandatory): titles and bodies always in English. If the user provides Spanish, translate preserving meaning (keep the emoji prefix).
- Branch slugs always in English: `Tipo/<issue>-english-short-desc` (max 4-5 words). Translate if the user provides Spanish.
- Link (mandatory): every work branch must be linked to its issue — open an early draft PR `branch → develop` with `Closes #<issue>` (title = exact branch name) and move project Status to `In progress`.
- Project option names (canonical, do not invent variants): Priority `🔴 Urgent | 🟠 High | 🟡 Medium`, Size `📏 Extra Small | 📏 Small | 📏 Medium | 📏 Large | 📏 Extra Large`.
- States: `backlog → ready (has acceptance + form label) → in-progress (branch linked) → review (PR linked) → done (acceptance checked + screenshots mobile+TV where UI)`.
- `ready` requires: form label set, acceptance criteria present, api impact stated (`new-api endpoint or none`). Bugs require reproducible steps + form factor.
- Close rule: UI issues close only with both mobile and TV evidence attached (or single-form justification from `parity-phone-tv`).

## Out of scope

- Do NOT define issue bodies here. See `issue-bug`, `issue-feature`, `issue-task`.
- Do NOT define PR body here. See `pr-description`.
- Do NOT define QA execution here. See `apply-qa-apk`.
