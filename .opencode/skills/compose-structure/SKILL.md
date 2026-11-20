---
name: compose-structure
description: Use when creating or editing any Compose Screen or ViewModel in miteve-apk to enforce route-state-viewmodel-content shape and UiState+Intent discipline.
license: MIT
compatibility: opencode
---

# Compose Structure — Screen/ViewModel shape only

This skill covers ONLY the per-screen file shape. It does NOT cover theming, navigation graph, or layer placement.

## Screen.kt checklist (`app/.../presentation/feature/home/HomeScreen.kt`)

- Route composable named `<Name>Screen` with signature `(onNavigate: (String) -> Unit, viewModel: HomeViewModel = hiltViewModel())` then a stateless `HomeContent(state, onIntent)` overload. Route < 60 lines, only composes.
- Collect state with `collectAsStateWithLifecycle()`, never bare `collectAsState()` in Screens (lifecycle-safe on TV standby + PiP).
- No business logic in `@Composable`: no `if (plan.interval == ...)` math, no Retrofit/Room, no token reads. Logic lives in ViewModel/use case.
- One parameter per line when >2 params (see `kotlin-format-android`).
- Every Screen ships at least one `@Preview` (phone) rendering `HomeContent` with fake state — no ViewModel in previews.

```kotlin
// app/src/main/kotlin/app/miteve/presentation/feature/home/HomeScreen.kt
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
) { /* renders state only */ }
```

## ViewModel checklist (`HomeViewModel.kt` + `HomeUiState.kt`)

- Single `UiState` data class + single `Intent` sealed interface per screen:
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/home/HomeUiState.kt
  data class HomeUiState(val isLoading: Boolean = true, val items: List<Content> = emptyList())
  sealed interface HomeIntent { data class Open(val id: String) : HomeIntent; data object Retry : HomeIntent }
  ```
- ViewModel exposes `val uiState: StateFlow<HomeUiState>` + `fun onIntent(intent: HomeIntent)`. Calls use cases, never repositories-of-data directly from UI, never holds `Context`/views.
- TV and phone share the SAME ViewModel and UiState (see `android-formfactors`). Screen variants differ; state never forks.

## Out of scope

- Do NOT define theming here. See `compose-theming`.
- Do NOT define TV focus handling here. See `compose-tv-pattern`.
- Do NOT define formatting here. See `kotlin-format-android`.
