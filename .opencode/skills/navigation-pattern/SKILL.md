---
name: navigation-pattern
description: Use when adding or changing destinations, deep links, or auth/admin guards in the miteve-apk Compose nav graph shared by phone and TV launchers.
license: MIT
compatibility: opencode
---

# Navigation Pattern — graph + guards only

This skill covers ONLY destinations, arguments, deep links, and guards. It does NOT cover Screen internals or auth token storage.

## Rules

- Single `NavHost` graph in `app/.../presentation/navigation/MiteveNavGraph.kt`, shared by both launchers. Destinations: `home`, `search`, `downloads`, `profile`, `player/{contentId}`, `admin`. Adding a destination = new route + ViewModel + UiState, registered once here.
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/navigation/MiteveNavGraph.kt
  composable(
      route = "player/{contentId}",
      arguments = listOf(navArgument("contentId") { type = NavType.StringType }),
      deepLinks = listOf(navDeepLink { uriPattern = "miteve://player/{contentId}" }),
  ) { backStackEntry ->
      PlayerEntryPoint(contentId = checkNotNull(backStackEntry.arguments?.getString("contentId")))
  }
  ```
- `PlayerEntryPoint` picks `PlayerMobileScreen` vs `PlayerTvScreen` by `isTvDevice()` but shares one `PlayerViewModel` (see `android-formfactors`). Deep link `miteve://player/{id}` lands on the same graph on both form factors.
- Guards (parity with web `proxy.ts`): `authRequired = setOf("downloads", "profile", "admin")`, `adminRequired = setOf("admin")`. Guard reads `SessionRepository` (DataStore token + `read-admin-page` privilege from api), redirects to `home` + Snackbar message on denial. Never rely on hiding nav items alone.
- Both `MobileActivity` (LAUNCHER) and `TvActivity` (LEANBACK_LAUNCHER) call `MiteveApp(navGraph)` — same graph, different entry route (`home` phone, `home` TV leanback). Keep the dual-launcher `AndroidManifest` pattern (lines 165-173 era) but with native activities, no `LauncherActivity` TWA.

## Out of scope

- Do NOT define auth token mechanics here. See `api-client-auth`.
- Do NOT define Screen layout here. See `compose-structure`, `compose-tv-pattern`.
- Do NOT define deep-link marketing URLs here beyond the `miteve://` scheme without updating both activities' intent-filters.
