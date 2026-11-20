---
name: kotlin-format-android
description: Use when creating or editing any Kotlin file in miteve-apk to enforce ktfmt style, import order, naming, and Compose state rules.
license: MIT
compatibility: opencode
---

# Kotlin Format Android — formatting only

Self-contained checklist for `.kt` cleanup in `app/src/main/kotlin/app/miteve/...`. Do NOT change business logic, only apply standards below. Goal: any dev can read and maintain it. App is 100% native Kotlin, NO WebView.

## 1. File order and sections

- Order top to bottom: `package` → imports → `const val` / companion constants → public types/functions → private helpers.
- Imports: `android.*`, `androidx.*`, `com.*`/`dagger.*`/`hilt` third-party, `java.*`/`javax.*`/`kotlin.*`, then `app.miteve.*`. Alphabetical within each group, no wildcard imports except Compose `material3.*` where idiomatic. ktfmt enforces this; run `./gradlew ktfmtCheck` before commit.
- One screen + one ViewModel per file max, matching filename: `app/.../feature/home/HomeScreen.kt` holds only `HomeScreen` route + `HomeContent`; `HomeViewModel.kt` separate file. Exception: tiny `UiState` data class may live with its ViewModel file.
- No blank lines between tightly coupled sections; one blank line between functions.

## 2. Naming and declarations

- All code and comments in English. Clear names: `continueWatching` not `contWatch`, `mapDtoToDomain` not `mapDto`.
- Variables/functions `camelCase`. Classes/objects/composables/data classes `PascalCase`. `ViewModel`/`Screen` suffix mandatory: `HomeViewModel`, `HomeScreen`, `PlayerTvScreen`. Use cases verb-first: `GetContinueWatchingUseCase`.
- Prefer `val` over `var`. `var` only for genuine mutation (e.g. player position holder inside ViewModel with `StateFlow`).
- Explicit types on public API: `fun loadHome(): Flow<HomeUiState>`. Local inference `val items = emptyList<Content>()` is fine.
- No `!!` operator. Use `?: return`, `?.let`, or `checkNotNull` with message in ViewModel init only:
  ```kotlin
  // app/src/main/kotlin/app/miteve/feature/player/PlayerViewModel.kt
  @HiltViewModel
  class PlayerViewModel @Inject constructor(
      private val getContent: GetContentUseCase,
      savedStateHandle: SavedStateHandle,
  ) : ViewModel() {
      private val contentId: String = checkNotNull(savedStateHandle["contentId"]) {
          "PlayerViewModel requires contentId"
      }
      private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
      val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()
  }
  ```
- State hoisting in every composable: stateless `ContentCard(title: String, artworkUrl: String, onClick: () -> Unit, modifier: Modifier = Modifier)` + stateful wrapper only at Screen level collecting ViewModel. Never `viewModel()` inside reusable components in `presentation/components/`.
- One parameter per line when a composable/function has >2 params:
  ```kotlin
  @Composable
  fun ContentCard(
      title: String,
      artworkUrl: String,
      onClick: () -> Unit,
      modifier: Modifier = Modifier,
  ) { /* ... */ }
  ```

## 3. Braces, comments, cleanup

- ktfmt style: 4-space indent, max width per `.editorconfig`. Trailing commas in multiline argument lists.
- Comment the *why*, not the *what*. Every `LaunchedEffect`, focus-restoration hack, and TV D-pad workaround gets one line explaining parity (e.g. `// Restores focus after rotation — parity with useFocusable.ts`).
- Delete unused code. No commented-out blocks. No `Log.e` scattering (see `error-handling-android`).

## 4. Verify

- After applying: `./gradlew ktfmtCheck app:assembleDebug` until zero errors. Full verification: `./gradlew build`. See `gradle-build-run-apk`.
- Spot check: `rg -n "!!" app/src/main/kotlin` must return zero hits.

## Out of scope

- Do NOT define layer placement here. See `android-clean-architecture`.
- Do NOT define Screen/ViewModel content rules here. See `compose-structure`.
- Do NOT define error mapping here. See `error-handling-android`.
- WebView is FORBIDDEN in app code (OAuth Custom Tab only exception). Never reintroduce `android.webkit.WebView` to bypass formatting or layout work.
