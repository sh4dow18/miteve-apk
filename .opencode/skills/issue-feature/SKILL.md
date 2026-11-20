---
name: issue-feature
description: Use when filing a feature request for miteve-apk covering both phone and TV twins, shared state, and acceptance criteria.
license: MIT
compatibility: opencode
---

# Issue Feature — feature request shape only

This skill covers ONLY feature issue bodies. Process mechanics live in `issue-flow`.

## Template

```markdown
## Problem (user + form factor)
<!-- Who needs it, on phone / TV / both -->

## Proposal
<!-- Mobile variant + TV variant + shared ViewModel/UiState — name the files -->

## Acceptance
- [ ] MobileScreen + TvScreen share route/ViewModel/UiState (parity-phone-tv)
- [ ] Works offline/online per room-sync (if data involved)
- [ ] Screenshots mobile + TV attached before close (apply-qa-apk)
```

- Every feature names BOTH twins or justifies a single-form-factor exception inline (mother skill `android-formfactors`).
- Reference the api endpoint or state `no api change` explicitly — never silent on backend impact.

## Out of scope

- Do NOT define task checklists here. See `issue-task`.
- Do NOT define workflow/labels here. See `issue-flow`.
