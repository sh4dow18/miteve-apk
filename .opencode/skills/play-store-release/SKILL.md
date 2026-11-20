---
name: play-store-release
description: Use when cutting a Play release in miteve-apk — fix broken keystore via local.properties, bundleRelease, versionCode/Name sync with web+api, tracks, phone + TV screenshots, android:banner.
license: MIT
compatibility: opencode
---

# Play Store Release — signing, bundle, tracks only

This skill covers ONLY release signing, bundling, and Play Console tracks. It does NOT cover version numbering policy (`version-tag`), Gradle mechanics (`gradle-build-run-apk`), secrets storage rules (`conn-secrets-android`), or QA flows (`apply-qa-apk`).

## 1. Fix keystore roto (OBLIGATORIO antes de bundlear)

- El keystore de referencia `/home/sh4dow18/...` NO existe en esta máquina. Nunca hardcodear rutas absolutas ajenas en `app/build.gradle.kts`.
- Toda firma release sale de `local.properties` (gitignored) + `BuildConfig`/`signingConfigs`. Patrón:
  ```kotlin
  // app/build.gradle.kts
  val localProps = java.util.Properties().apply {
      val f = rootProject.file("local.properties")
      if (f.exists()) f.inputStream().use(::load)
  }
  signingConfigs {
      create("release") {
          storeFile = file(localProps.getProperty("KEYSTORE_PATH") ?: error("Falta KEYSTORE_PATH en local.properties"))
          storePassword = localProps.getProperty("KEYSTORE_PASSWORD") ?: error("Falta KEYSTORE_PASSWORD")
          keyAlias = localProps.getProperty("KEY_ALIAS") ?: error("Falta KEY_ALIAS")
          keyPassword = localProps.getProperty("KEY_PASSWORD") ?: error("Falta KEY_PASSWORD")
      }
  }
  buildTypes {
      release {
          signingConfig = signingConfigs.getByName("release")
          isMinifyEnabled = true
          isShrinkResources = true
          proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
  }
  ```
  ```properties
  # local.properties (NUNCA commitear)
  KEYSTORE_PATH=C:/Users/ramgi/keystores/miteve-release.jks
  KEYSTORE_PASSWORD=***
  KEY_ALIAS=miteve
  KEY_PASSWORD=***
  ```
- Verificación: `./gradlew app:tasks --all | findstr bundle` lista `bundleProdRelease`; `signingReport` muestra SHA-1/SHA-256 del cert release. Si falla, STOP — prohibido taggear (ver `version-tag`).

## 2. Orden release (checklist accionable)

1. Sync versiones: `versionCode` +1 monotónico, `versionName` = web `package.json` = api release (ver `version-tag`).
2. `./gradlew app:bundleProdRelease` en verde (single APK, dual launcher `MobileActivity` + `TvActivity` LEANBACK, `applicationId app.vercel.miteve.twa`, `minSdk 26`).
3. Subir `.aab` a track: `internal` → `closed` → `production` (promoción gradual, nunca directo a production).
4. Adjuntar screenshots phone + TV Leanback + `android:banner` verificado (sección 3).
5. Release notes ES + enlace a issue/PR (`pr-description`, `git-flow`).

## 3. Naming — screenshots + banner TV

- Phone: mínimo 2 capturas (portrait 1080x2400): home + player. TV Leanback: mínimo 2 (1920x1080): browse + details. Guardar en `docs/release/vX.Y.Z/phone_*.png`, `docs/release/vX.Y.Z/tv_*.png`.
- `android:banner` OBLIGATORIO para launcher TV, PNG 320x180 (xhdpi) en `res/drawable-xhdpi/banner.png`:
  ```xml
  <!-- app/src/main/AndroidManifest.xml -->
  <activity android:name="app.miteve.presentation.tv.TvActivity"
      android:banner="@drawable/banner"
      android:exported="true">
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
      </intent-filter>
  </activity>
  ```
- Verificación: instalar en emulador TV → banner visible en home Leanback; sin banner el listing TV es rechazado.

## 4. Verificación pre-release (todo ✅ o no se sube)

- [ ] `bundleProdRelease` verde + `mapping.txt` guardado para Crashlytics (`crashlytics-analytics`).
- [ ] `versionCode` no reutilizado, `versionName` == web + api.
- [ ] SHA-256 del cert release == huellas en Play Console + `assetlinks.json` (`applinks-assetlinks`).
- [ ] Screenshots phone + TV adjuntos, banner renderiza.
- [ ] Rollout `internal` testeado antes de `closed`/`production`.

## Out of scope

- Do NOT define version/tag policy here. See `version-tag`.
- Do NOT define QA adversarial flows here. See `apply-qa-apk`.
- Do NOT define key storage conventions here. See `conn-secrets-android`.
- Do NOT use WebView para pantallas de release — prohibido salvo OAuth/Custom Tabs.
