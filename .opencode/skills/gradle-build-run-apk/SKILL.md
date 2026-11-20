---
name: gradle-build-run-apk
description: Use when building, running, signing, or changing Gradle config in miteve-apk (Kotlin DSL, version catalog, flavors, SDK levels, keystore).
license: MIT
compatibility: opencode
---

# Gradle Build Run APK — build and run only

This skill covers ONLY Gradle build/run/sign config for the native app. It does NOT cover Kotlin style, architecture, or secrets content.

## Rules

- Kotlin DSL only. `app/build.gradle` (Groovy, AGP 8.9.1 legacy) migrates to `app/build.gradle.kts`. Root `settings.gradle` → `settings.gradle.kts` with version catalog `gradle/libs.versions.toml`:
  ```toml
  # gradle/libs.versions.toml
  [versions]
  agp = "8.9.1"
  kotlin = "2.1.20"
  composeBom = "2025.04.00"
  media3 = "1.5.1"
  room = "2.6.1"
  hilt = "2.55"
  [libraries]
  androidx-tv-foundation = { module = "androidx.tv:tv-foundation:1.0.0" }
  androidx-tv-material = { module = "androidx.tv:tv-material:1.0.0" }
  media3-exoplayer-dash = { module = "androidx.media3:media3-exoplayer-dash:1.5.1" }
  ```
- SDK levels (target file `app/build.gradle.kts`): `compileSdk = 36`, `targetSdk = 35`, `minSdk = 26` (raised from TWA legacy 21 — required for DataStore + Media3 stable + Compose TV). Never lower `minSdk` without a skill update.
- Java 17+ toolchain: `kotlin { jvmToolchain(17) }` + `compileOptions { sourceCompatibility = JavaVersion.VERSION_17 }`. Legacy `JavaVersion.VERSION_1_8` is removed during migration.
- Commands: `./gradlew app:assembleDevDebug` (dev, points at `https://miteve.vercel.app`), `./gradlew app:assembleProdRelease` / `bundleProdRelease` for Play. Verify: `./gradlew build`. List tasks with `./gradlew tasks --all`, never invent task names.
- Flavors in `app/build.gradle.kts`, same `applicationId` for Play continuity (`app.vercel.miteve.twa` preserved — do NOT change or Play Store listing is lost):
  ```kotlin
  flavorDimensions += "env"
  productFlavors {
      create("dev") { dimension = "env"; applicationIdSuffix = ".dev"; buildConfigField("String", "API_HOST", "\"https://miteve.vercel.app\"") }
      create("prod") { dimension = "env"; buildConfigField("String", "API_HOST", "\"https://miteve.vercel.app\"") }
  }
  ```
  Local backend override is a `local.properties` key (`api.host=http://10.0.2.2:8080`), never a committed flavor.
- Signing: fix the BROKEN Linux keystore path from TWA era (`/home/sh4dow18/.../android.keystore` does not exist on Windows/CI). New rule — `local.properties` keys `store.file`, `store.password`, `key.alias`, `key.password`, resolved with fallback to debug keystore:
  ```kotlin
  val localProps = java.util.Properties().apply {
      rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
  }
  signingConfigs {
      create("release") {
          val storeFileProp = localProps.getProperty("store.file")
          if (!storeFileProp.isNullOrBlank()) {
              storeFile = rootProject.file(storeFileProp)
              storePassword = localProps.getProperty("store.password")
              keyAlias = localProps.getProperty("key.alias")
              keyPassword = localProps.getProperty("key.password")
          }
      }
  }
  ```
  Missing release keys → build signs debug and warns; CI release requires the four keys. See `conn-secrets-android`.

## Out of scope

- Do NOT define secret values here. See `conn-secrets-android`.
- Do NOT define version bump policy here. See `version-tag`.
- Do NOT touch Kotlin code style here. See `kotlin-format-android`.
