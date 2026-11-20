---
name: issue-task
description: Use when filing a chore or refactor task for miteve-apk (Gradle migration, keystore fix, renames, tooling).
license: MIT
compatibility: opencode
---

# Issue Task — chore/refactor shape only

This skill covers ONLY task issue bodies. Process mechanics live in `issue-flow`.

## Template

```markdown
## Goal
<!-- One sentence, no user story needed -->

## Scope (files/dirs)
<!-- e.g. app/build.gradle.kts, gradle/libs.versions.toml -->

## Done when
- [ ] ./gradlew build green
- [ ] No behavior change (or named behavior change + tests)
```

- Tasks never carry user-facing acceptance; if they do, file `issue-feature` instead.
- Keystore/signing tasks must reference `conn-secrets-android` + `gradle-build-run-apk` and never paste secret values.

## Out of scope

- Do NOT define workflow/labels here. See `issue-flow`.
- Do NOT define build commands detail here. See `gradle-build-run-apk`.
