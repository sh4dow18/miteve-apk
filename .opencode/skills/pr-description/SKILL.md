---
name: pr-description
description: Use when opening or reviewing any pull request in miteve-apk to enforce linked issue, phone+TV evidence, and layer summary.
license: MIT
compatibility: opencode
---

# PR Description — PR body only

This skill covers ONLY PR bodies. Branch mechanics live in `git-flow`.

## Template (all sections mandatory)

```markdown
## Closes
<!-- #<issue> -->

## What (layers touched)
<!-- presentation / domain / data / build — per android-clean-architecture -->

## Parity
<!-- phone twin + TV twin status, or justified single-form exception -->

## Evidence
<!-- ./gradlew build result + mobile screenshot + TV screenshot (UI PRs) -->
```

- One issue per PR. Phone+TV travel together — split PRs for the same feature are rejected (prevents drift).
- Evidence: paste the exact `./gradlew build` tail (BUILD SUCCESSFUL) and attach both screenshots for UI changes. No `works on my machine` without logs.
- Reviewer runs the `parity-phone-tv` checklist before approving.

## Out of scope

- Do NOT define branch naming here. See `git-flow`.
- Do NOT define versioning here. See `version-tag`.
- Do NOT define QA steps here. See `apply-qa-apk`.
