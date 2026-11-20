---
name: android-formfactors
description: Use when deciding what to share vs duplicate between phone and Android TV in miteve-apk (mother skill for Mobile vs TV variants, single APK dual launcher).
license: MIT
compatibility: opencode
---

# Android Form Factors — share vs duplicate (mother skill)

This skill is the MOTHER decision rule for phone + TV. It covers ONLY the share/duplicate boundary and the single-APK dual-launcher contract. Details live in `compose-tv-pattern` and `parity-phone-tv`.

## Rule 1 — ALWAYS share (one implementation, both form factors)

- `ViewModel` + `UiState` + `Intent`, all `domain/` use cases/models/repository interfaces, all `data/` (Retrofit, Room, DataStore, WorkManager), navigation graph + guards, theme tokens.
- Consequence: a bug fix in `GetContinueWatchingUseCase` fixes phone and TV at once. If you find yourself copying a ViewModel for TV, stop — you are desynchronizing.

## Rule 2 — ALWAYS duplicate (separate files, same state)

- Every user-facing Screen with different interaction: `HomeMobileScreen.kt` vs `HomeTvScreen.kt`, `ContentCard.kt` (touch, `WindowSizeClass`-adaptive) vs `ContentCardTv.kt` (focus-scaled), `PlayerMobileScreen.kt` (touch gestures) vs `PlayerTvScreen.kt` (D-pad). Entry point picks by `isTvDevice()` / `UiModeManager.UI_MODE_TYPE_TELEVISION`.
- Phone layouts adapt with `WindowSizeClass` (compact/medium/expanded); TV NEVER uses `WindowSizeClass` — TV uses 10-foot rules from `compose-tv-pattern`.

## Rule 3 — FORBIDDEN: one layout for everything

- A single `HomeScreen` with `if (isTv)` branches scattered through layout code is prohibited. One branch at the entry point, separate files below. Audit with `parity-phone-tv`.

## Single APK, dual launcher (native, preserves Manifest 165-173 pattern)

```xml
<!-- app/src/main/AndroidManifest.xml — same applicationId, no TWA activity -->
<activity android:name=".presentation.MobileActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:name=".presentation.TvActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
    </intent-filter>
</activity>
```

Both activities call the same `MiteveApp()` graph. No WebView in either (see global ban in `kotlin-format-android`).

## Out of scope

- Do NOT define TV focus APIs here. See `compose-tv-pattern`.
- Do NOT define the audit checklist here. See `parity-phone-tv`.
- Do NOT define build flavors here. See `gradle-build-run-apk`.
