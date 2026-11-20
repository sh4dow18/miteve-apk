---
name: compose-theming
description: Use when styling any Compose UI in miteve-apk to enforce Material3 dark theme parity with miteve-web (#141414) and centralized tokens.
license: MIT
compatibility: opencode
---

# Compose Theming — Material3 tokens only

This skill covers ONLY theme/tokens/styling. It does NOT cover Screen shape, layout, or navigation.

## Rules

- Single source of truth: `app/.../core/ui/theme/Theme.kt` + `Color.kt` + `Type.kt`. Dark scheme primary background `#141414` (parity with web `@theme` base). Light scheme defined but app defaults to dark like the web app.
  ```kotlin
  // app/src/main/kotlin/app/miteve/core/ui/theme/Color.kt
  val BackgroundDark = Color(0xFF141414)
  // app/src/main/kotlin/app/miteve/core/ui/theme/Theme.kt
  private val DarkScheme = darkColorScheme(background = BackgroundDark /* ... */)
  @Composable
  fun MiteveTheme(content: @Composable () -> Unit) {
      MaterialTheme(colorScheme = DarkScheme, typography = MiteveTypography, content = content)
  }
  ```
- Typography/colors centralized. No hex literals in Screens/components: `rg -n "Color\(0x" app/src/main/kotlin --glob '!core/ui/theme/*'` must return zero hits. No `MaterialTheme.colorScheme` bypass with ad-hoc `copy(alpha)` chains longer than one call.
- Web `Toaster` → native `SnackbarHost` + `SnackbarHostState` owned by scaffold, driven by `UiState.message` (see `error-handling-android`). Never `Toast.makeText` for user-facing errors.
- Icons: Lucide (web) → Compose Material Icons / `ImageVector` in `core/ui/icons/`. No PNG icons for actions that exist as vectors. `store_icon.png` stays launcher-only.
- TV uses `androidx.tv.material3` theme wrapper (`TvMiteveTheme`) reusing the same color tokens, not a forked palette (see `compose-tv-pattern`).

## Out of scope

- Do NOT define Screen structure here. See `compose-structure`.
- Do NOT define TV focus/D-pad here. See `compose-tv-pattern`.
- Do NOT define error mapping here. See `error-handling-android`.
