---
name: version-tag
description: Use when releasing miteve-apk to keep versionCode/Name in sync with miteve-web package.json and miteve-api, and to cut release tags safely.
license: MIT
compatibility: opencode
---

# Version Tag — versioning and releases only

This skill covers ONLY version bumps and release tags. Build mechanics live in `gradle-build-run-apk`.

## Rules

- Single version truth per release: `versionName` (e.g. `1.4.0`) matches web `package.json` version and api release version for the same milestone; `versionCode` increments monotonically every release (never reuse, never decrease — Play rejects otherwise). Set in `app/build.gradle.kts` `defaultConfig`.
- Bump checklist: update `versionName`+`versionCode` → update web/api cross-refs in release notes → `./gradlew app:bundleProdRelease` green → tag.
- Tags `release/X.Y.Z` are created ONLY in the GitHub UI (web release flow), never via local `git tag` + push. Tagging a broken keystore build is forbidden: verify `signingConfigs.release` resolves (see `gradle-build-run-apk` keystore fix) before cutting the release.
- Release PR includes the parity table (`parity-phone-tv`) and both emulator screenshots (`apply-qa-apk`).

## Out of scope

- Do NOT define signing keys here. See `conn-secrets-android`.
- Do NOT define branch flow here. See `git-flow`.
