---
name: srp-audit-kotlin
description: Use when auditing Kotlin files in miteve-apk for single-responsibility violations (god ViewModels, logic in composables, mixed layers).
license: MIT
compatibility: opencode
---

# SRP Audit Kotlin — responsibility audit only

This skill covers ONLY detecting and splitting responsibility violations. It does NOT fix formatting (`kotlin-format-android`) or move files across layers (`android-clean-architecture`).

## Audit checklist (per file, `app/src/main/kotlin/app/miteve/...`)

- [ ] ViewModel owns exactly one screen's state: one `UiState` + one `Intent` type. Two unrelated `StateFlow`s (e.g. player + downloads in one VM) → split.
- [ ] No data-layer imports in `presentation/`: `rg -n "retrofit|room|okhttp|dao" <ScreenOrViewModel>.kt -i` must be empty (case-insensitive). Hits move to `domain/usecase` + `data/`.
- [ ] No business math in `@Composable`: search `if.*==` pricing/seek/progress logic in Screens — moves to `domain/` pure function or ViewModel.
- [ ] Composable under ~120 lines; Screen route under 60. Larger → extract to `presentation/components/` (phone) or `*Tv.kt` twin (TV).
- [ ] One reason to change: describe it in one sentence (`HomeViewModel changes only when home state shape changes`). If you need `and`, split.

## Report shape

`| file | violation | split into | target skill |` — one row per violation, then apply the fix under the target skill (`compose-structure`, `android-clean-architecture`, `player-offline`, etc.).

## Out of scope

- Do NOT reformat code here. See `kotlin-format-android`.
- Do NOT define error mapping here. See `error-handling-android`.
- WebView-based shortcuts (dumping logic into a web page instead of splitting) are forbidden.
