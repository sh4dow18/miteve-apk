# AGENTS.md — miteve-apk

Kotlin + Compose + Compose TV, single APK dual launcher (phone/TV). Flavors `dev`/`prod`, `compileSdk 36`, AGP 8.9.1, JDK 17.

## Commands

- Build dev (diaria): `./gradlew app:assembleDevDebug` · instalar: `./gradlew app:installDevDebug`.
- Release: `./gradlew app:assembleProdRelease` / `./gradlew app:bundleProdRelease` (requiere keystore en `local.properties`, nunca commiteado).
- Tests: `./gradlew app:connectedDevDebugAndroidTest` (instrumentados; en CI es allow-failure sin emulador) · lint: `./gradlew app:lintDevDebug`.
- Verificación rápida antes de commitear: `assembleDevDebug` en verde + probado en emuladores phone y TV.

## Wiring (read before changing)

- Launchers: `MobileActivity` (`LAUNCHER`, phone: touch/PiP) + `TvActivity` (`LEANBACK_LAUNCHER` + `android:banner`, TV: D-pad/foco/overscan). Cada una con su nav graph; las Screens gemelas comparten `ViewModel` + `UiState` (ej: `PlayerScreen`/`TvPlayerScreen` → `PlayerViewModel`/`PlayerUiState`).
- Red: Retrofit contra `BuildConfig.API_HOST` (`dev` → `https://dev-api.miteve.vercel.app`, `prod` → `https://api.miteve.vercel.app`). Nunca hardcodear hosts.
- Offline: Media3/ExoPlayer DASH + Room + WorkManager (constraints WiFi + charging) vía `domain` use cases (ver `player-offline`, `room-sync`, `download-manager-ux`).
- Arquitectura: `presentation (Mobile + TV) → domain ← data`. UI solo consume `UiState`/`Intent`; sin SQL ni HTTP en `presentation`.
- Secretos: `local.properties` (sdk.dir + keystore) fuera del repo (ver `conn-secrets-android`).

## Conventions (enforced in review)

- `kotlin-format-android` (ktfmt) + `compose-structure` (ruta-estado-viewmodel-contenido, `UiState`+`Intent`); theming `compose-theming` (Material3 dark `#141414`, tokens centralizados); TV con `compose-tv-pattern` (foco visible, D-pad completo, overscan).
- Paridad obligatoria: toda Screen phone tiene gemela TV (o justificación según `parity-phone-tv`); cada PR trae sección `Phone vs TV` con screenshots + foco.
- Errores vía `Result<T>` + `ErrorHandler` con Snackbar (`error-handling-android`); texto visible localizable; código y commits en inglés.

## Git guardrails (CI blocks on violation)

- Branch desde `develop`: `Tipo/<issue>-descripcion-corta`, tipos `Feature|Fix|Refactor|Docs` (capitalized). Exentas: `main`, `develop`, `release/*`, `hotfix/*`.
- Commit: `#<issue> <tipo>: <desc>` en inglés minúsculas, tipos `feat|fix|refactor|docs|style|test|chore`. El `#` debe coincidir con la rama; `Docs/*` solo acepta `docs:`.
- PR: título = nombre exacto de la rama; base `develop`; plantilla completa + `Phone vs TV` (Mobile PASS + TV PASS con foco + comando). Merges: `develop` 1 aprobación, `main`/`release/*` 2. Reviewer auto: `sh4dow18`.

## Do-not-touch rules

- Versión solo en `release/*`/`hotfix/*`: `versionCode` (+1 siempre) + `versionName` (`X.Y.Z`) en `app/build.gradle`, en sync con miteve-web (`package.json`) y miteve-api. Tags/releases solo desde GitHub UI (`vX.Y.Z`), nunca local.
- Full contributor docs: `documentation/contributing/` (guías numeradas) + `CONTRIBUTING.md`. No tocar `.opencode/skills`.
