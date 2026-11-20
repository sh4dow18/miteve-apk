---
name: parity-phone-tv
description: Use when auditing that every phone Screen has its TV twin (or a justified exception) sharing route, ViewModel, and UiState in miteve-apk.
license: MIT
compatibility: opencode
---

# Parity Phone TV — drift audit only

This skill covers ONLY the phone↔TV consistency audit. It does NOT cover building TV UI (`compose-tv-pattern`) or the share rule itself (`android-formfactors`).

## Audit checklist (run per feature, prevents web-style `*TV.tsx` drift)

- [ ] Every `*MobileScreen.kt` has a `*TvScreen.kt` twin, OR a one-line justification in the feature README (e.g. `AdminTvScreen: justified absent — admin is phone-only, TV nav hides entry AND graph guard denies`).
- [ ] Same route string, same `ViewModel`, same `UiState`/`Intent` types imported by both twins. `rg -n "UiState" app/.../presentation/feature/<name>/` shows ONE UiState file.
- [ ] Same user-visible data: same repository/use-case calls, same empty/error/loading states (both render `UiState.message` via Snackbar — TV included).
- [ ] Twin naming: `<Name>MobileScreen.kt` / `<Name>TvScreen.kt`, components `<Name>.kt` / `<Name>Tv.kt` (mirrors web `ContentCard` vs `ContentCardTV`, `Player` vs `PlayerTV`).
- [ ] Entry point branches exactly once: `if (isTvDevice()) HomeTvScreen(...) else HomeMobileScreen(...)` with shared `viewModel.uiState`.

## How to run

```bash
ls app/src/main/kotlin/app/miteve/presentation/feature/home/
# expect: HomeMobileScreen.kt HomeTvScreen.kt HomeViewModel.kt HomeUiState.kt
rg -l "isTvDevice|UI_MODE_TYPE_TELEVISION" app/src/main/kotlin | sort
# every branch site must reference the audit table below
```

Record results as a table in the PR/issue: `| feature | mobile | tv / justified absent | shared VM+state |`.

## Out of scope

- Do NOT fix TV focus internals here. See `compose-tv-pattern`.
- Do NOT change the nav graph here. See `navigation-pattern`.
- Do NOT touch player specifics here. See `player-offline`.
