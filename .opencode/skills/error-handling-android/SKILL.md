---
name: error-handling-android
description: Use when adding or changing error propagation in miteve-apk (Result types, ErrorHandler, Snackbar surfacing).
license: MIT
compatibility: opencode
---

# Error Handling Android — Result + Snackbar only

This skill covers ONLY error modeling and surfacing. It does NOT cover logging config or auth retry mechanics.

## Rules

- All repository/use-case returns are `ApiResult<T>` (`app/.../core/result/ApiResult.kt`): `Success(data) | ApiError(code, message) | NetworkError | Unauthorized`. No bare `try/catch + Log.e` at call sites, no nullable-swallowing (`?: null` without mapping).
  ```kotlin
  // app/src/main/kotlin/app/miteve/core/result/ApiResult.kt
  sealed interface ApiResult<out T> {
      data class Success<T>(val data: T) : ApiResult<T>
      data class ApiError(val code: Int?, val message: String) : ApiResult<Nothing>
      data object NetworkError : ApiResult<Nothing>
      data object Unauthorized : ApiResult<Nothing>
  }
  ```
- Single `ErrorHandler` (`core/error/ErrorHandler.kt`) maps `Throwable`/HTTP codes to user strings — parity with api `ErrorHandler.kt` codes. ViewModels convert `ApiResult` → `UiState(message)`; Screens render `message` via `SnackbarHost`, never `Toast`, never raw exception text.
- `Unauthorized` funnels to session logout (see `api-client-auth`), not to per-screen login dialogs.
- Verify: `rg -n "try ?\{" app/src/main/kotlin --glob '!core/error/*' --glob '!data/api/*'` — every hit outside the interceptor/mapper boundary must map into `ApiResult`, not `Log.e` + swallow.

## Out of scope

- Do NOT define auth refresh here. See `api-client-auth`.
- Do NOT define Snackbar theming here. See `compose-theming`.
- Do NOT scatter `try/catch (e: Exception) { Log.e(...) }` — forbidden by this skill.
