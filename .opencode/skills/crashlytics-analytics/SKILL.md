---
name: crashlytics-analytics
description: Use when wiring Crashlytics plus separated phone/TV analytics in miteve-apk — hashed userId without email, player/offline/cast events, opt-out, mapping upload on release.
license: MIT
compatibility: opencode
---

# Crashlytics + Analytics — crashes y eventos only

This skill covers ONLY crash reporting y analytics. No cubre performance (`baseline-performance`) ni push (`firebase-messaging`).

## 1. Checklist accionable

- Crashlytics ON en release, OFF en debug; `mapping.txt` subido por cada `bundleProdRelease` (`play-store-release`):
  ```kotlin
  // app/build.gradle.kts
  plugins { id("com.google.firebase.crashlytics") }
  // ./gradlew app:bundleProdRelease app:uploadCrashlyticsMappingFileProdRelease
  ```
- Analytics separadas phone/TV: propiedad `form_factor={phone,tv}` en CADA evento + instancia/pantalla separada en dashboard:
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/analytics/AnalyticsTracker.kt
  class AnalyticsTracker @Inject constructor() {
      fun track(event: String, formFactor: String, params: Map<String, String> = emptyMap()) {
          // firebaseAnalytics.logEvent(event, Bundle().apply {
          //   putString("form_factor", formFactor); params.forEach { (k,v) -> putString(k,v) } })
      }
      // Eventos fijos: player_start/player_error/offline_play/cast_start/cast_disconnect/download_complete
  }
  ```

## 2. Privacidad (OBLIGATORIO)

- `userId` = SHA-256 del id interno, NUNCA email/nombre:
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/analytics/IdentityHasher.kt
  fun hashedUserId(internalId: String): String = sha256("miteve|$internalId") // setUserId(hashed)
  ```
- Opt-out en ajustes ("Compartir estadísticas", default ON, exigible): `setAnalyticsCollectionEnabled(false)` + `crashlytics.setCrashlyticsCollectionEnabled(false)`. Sin PII en `customKeys` ni breadcrumbs.

## 3. Naming

- `data/analytics/AnalyticsTracker.kt`, `IdentityHasher.kt`; claves `form_factor`, `content_id`, `error_code`. Cero `email`/`name`/`token` como keys.

## 4. Verificación

- [ ] Release force-crash de prueba aparece en Crashlytics con mapping desofuscado.
- [ ] Dashboard filtra `form_factor=tv` vs `phone` sin mezclar.
- [ ] Opt-out → cero eventos en logcat/`adb` tras toggle.
- [ ] `grep -ri "email" data/analytics/` vacío (sin PII).

## Out of scope

- Do NOT definir Baseline/R8 aquí. See `baseline-performance`.
- Do NOT definir topics FCM aquí. See `firebase-messaging`.
- Do NOT loggear desde WebView — prohibido salvo OAuth/Custom Tabs.
