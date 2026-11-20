---
name: compose-tv-pattern
description: Use when building any Android TV UI in miteve-apk with Compose TV (androidx.tv foundation/material, focus, D-pad, overscan, 10-foot).
license: MIT
compatibility: opencode
---

# Compose TV Pattern — TV UI only

This skill covers ONLY TV composables (`*TvScreen.kt`, `*TV.kt`). Phone UI is governed by `compose-structure`. Sharing rules by `android-formfactors`.

## Rules

- TV foundation/material only in TV files: `androidx.tv.foundation.lazy.list.TvLazyRow/TvLazyColumn`, `androidx.tv.material3.*`. Never `androidx.compose.foundation.lazy.LazyRow` for the TV focus path — it breaks D-pad focus restoration.
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/home/HomeTvScreen.kt
  @Composable
  fun HomeTvScreen(state: HomeUiState, onIntent: (HomeIntent) -> Unit) {
      val focusRequester = remember { FocusRequester() }
      TvLazyRow(modifier = Modifier.onPreviewKeyEvent { event ->
          if (event.key == Key.DirectionCenter && event.type == KeyEventType.KeyUp) { /* select focused */ false } else false
      }) {
          items(state.items, key = { it.id }) { item ->
              ContentCardTv(title = item.title, artworkUrl = item.artworkUrl, onClick = { onIntent(HomeIntent.Open(item.id)) })
          }
      }
      LaunchedEffect(Unit) { focusRequester.requestFocus() } // restore initial focus — parity with useFocusable.ts
  }
  ```
- D-pad contract (parity with web `useFocusable.ts` + `useKeyboardNavigation.ts` + `useTV`): every focusable has a visible focused state (scale 1.1 + border), focus is never lost (initial `FocusRequester`, restore after data load/error), Enter/D-pad-center activates, Back pops nav (never exits app from non-root). Test on TV emulator with keyboard arrows, not mouse clicks.
- Overscan: root TV padding `48.dp` horizontal / `27.dp` vertical minimum; no critical text or controls outside the safe area.
- Text minimum `14.sp` on TV; focusable rows minimum `48.dp` hit height. Leanback theme wrapper `TvMiteveTheme` reuses phone color tokens (see `compose-theming`), not a forked palette.
- Same `ViewModel` + `UiState` as the Mobile twin (see `android-formfactors`). TV file imports the shared `HomeUiState`/`HomeIntent`, never redeclares state.

## Verify

- Run on Android TV emulator, navigate the whole screen with arrows + Enter only. Focus must never vanish; screenshots compared in `apply-qa-apk`.

## Out of scope

- Do NOT define the share/duplicate decision here. See `android-formfactors`.
- Do NOT define the sync audit here. See `parity-phone-tv`.
- Do NOT use WebView for TV playback or auth. Media3 only (see `player-offline`).
