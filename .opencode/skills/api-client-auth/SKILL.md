---
name: api-client-auth
description: Use when wiring Retrofit/OkHttp, JWT token storage, role guards, or API host config in miteve-apk.
license: MIT
compatibility: opencode
---

# API Client Auth — network + auth only

This skill covers ONLY the native HTTP client and JWT/role parity with `miteve-web/proxy.ts` + `miteve-api/SecurityConfig`. It does NOT cover UI, Room schema, or secret values.

## Rules

- Single `Retrofit` instance via Hilt (`app/.../data/api/MiteveApi.kt`, `di/NetworkModule.kt`). Base URL from `BuildConfig.API_HOST` (flavor-driven, see `gradle-build-run-apk`). Never hardcode `https://miteve.vercel.app` or `localhost` in Kotlin.
- JWT parity: token key `miteve_token` stored in **DataStore (Proto or Preferences)**, never `SharedPreferences` plaintext for tokens, never hardcode. `AuthInterceptor` attaches `Authorization: Bearer <token>`; `Authenticator` attempts one refresh then clears session and emits `LoggedOut` (parity with web dual `localStorage + cookie miteve_token` — native uses DataStore as the single holder, cookie jar only for refresh endpoint if api requires it):
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/api/AuthInterceptor.kt
  class AuthInterceptor @Inject constructor(
      private val tokenStore: TokenStore,
  ) : Interceptor {
      override fun intercept(chain: Interceptor.Chain): Response {
          val token = runBlocking { tokenStore.tokenFlow().firstOrNull() }
          val request = if (token.isNullOrBlank()) chain.request()
              else chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
          return chain.proceed(request)
      }
  }
  ```
- Roles parity: `Role`/`Privilege` enums mirror api (`domain/auth/Role.kt`); `read-admin-page` privilege gates the `admin` nav destination AND the backend call (see `navigation-pattern` guards). Never gate admin by hiding a button only — guard the route and the repository call.
- Init/`!init` parity: api `SecurityConfig` permits public `init` endpoints unauthenticated; native `MiteveApi` marks them `@NoAuth` (custom annotation skipped by interceptor) exactly matching the api allow-list. Changing the allow-list requires checking api `SecurityConfig` first.
- CORS/host: emulator talks to host via `10.0.2.2` or `ip.domain` override in `local.properties` (`api.host`), never `localhost` literal (localhost inside emulator is the emulator itself).
- No secrets in git: tokens at runtime only, API keys via `local.properties` + `BuildConfig`. See `conn-secrets-android`.

## Out of scope

- Do NOT define layer placement here. See `android-clean-architecture`.
- Do NOT define nav graph shape here. See `navigation-pattern`.
- Do NOT define error UI mapping here. See `error-handling-android`.
- WebView is forbidden for auth (no login-via-WebView). OAuth uses Custom Tabs; session returns via deep link to `MobileActivity`.
