---
name: baseline-performance
description: Use when optimizing startup and jank in miteve-apk — R8 minify, Baseline Profiles, Coil plus Media3 cache, low-end phone versus TV, sub-2s cold start.
license: MIT
compatibility: opencode
---

# Baseline Performance — arranque + jank only

This skill covers ONLY startup/jank/caching. It does NOT cover build flavors (`gradle-build-run-apk`), Coil wiring genérico, o Room schema (`room-sync`).

## 1. Checklist accionable

- R8 ON en release (obligatorio para Baseline + mapping de `crashlytics-analytics`):
  ```kotlin
  // app/build.gradle.kts
  release { isMinifyEnabled = true; isShrinkResources = true }
  ```
- Baseline Profiles: reglas en `app/src/main/baseline-prof.txt` + generador:
  ```kotlin
  // app/src/benchmark/kotlin/app/miteve/benchmark/StartupBenchmark.kt
  @Test fun startup() = benchmarkRule.measureRepeated(
      packageName = "app.vercel.miteve.twa",
      metrics = listOf(StartupTimingMetric()),
      iterations = 5,
      startupMode = StartupMode.COLD) { startActivityAndWait() }
  ```
  Generar con `./gradlew :app:generateBaselineProfile`, commitear el `.txt`.
- Coil + Media3 cache: Coil `ImageLoader` singleton con disk cache 250MB; ExoPlayer `SimpleCache` 1GB (`LeastRecentlyUsedCacheEvictor`) en `PlayerHolder` (`player-offline`):
  ```kotlin
  // app/src/main/kotlin/app/miteve/di/CacheModule.kt
  @Provides @Singleton fun imageLoader(@ApplicationContext ctx: Context): ImageLoader =
      ImageLoader.Builder(ctx).diskCache { DiskCache.Builder().directory(ctx.cacheDir.resolve("img")).maxSizeBytes(250L*1024*1024).build() }.build()
  ```

## 2. Jank: phone gama baja vs TV

- Phone gama baja: `LazyColumn` con `key` + `contentType`, `remember` en items pesados, cero `collectAsState` por celda; medir con `JankStats` en debug.
- TV: filas Leanback (`TvLazyRow`) paginan de 20 en 20; precarga de artwork del foco siguiente, no de toda la fila. Focusessel no debe caer frames al navegar rápido con D-pad (`compose-tv-pattern`).

## 3. Naming

- Métrica única: **cold start < 2s** (de tap a primer frame interactivo home) en Pixel 4a + Android TV 1080p. Macrobenchmark en `src/benchmark/`, perfiles en `src/main/baseline-prof.txt`.

## 4. Verificación

- [ ] `./gradlew :app:generateBaselineProfile` regenera sin errores; `bundleProdRelease` incluye el perfil.
- [ ] Macrobenchmark COLD < 2s en ambas formas (phone + TV).
- [ ] Sin regresión: `connectedDevDebugAndroidTest` + scroll sin jank visible en gama baja.
- [ ] Cache hit rate Coil/Media3 > 80% en sesión de revisión (logcat `Cache` en debug).

## Out of scope

- Do NOT definir descargas offline aquí. See `player-offline` + `download-manager-ux`.
- Do NOT definir eventos de analytics aquí. See `crashlytics-analytics`.
- Do NOT precargar vistas en WebView — prohibido salvo OAuth/Custom Tabs.
