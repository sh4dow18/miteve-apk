---
name: applinks-assetlinks
description: Use when wiring verified App Links in miteve-apk — miteve.vercel.app assetlinks.json, autoVerify intent-filters, player deep-links, TWA parity, Custom Tab fallback only for OAuth.
license: MIT
compatibility: opencode
---

# App Links + Assetlinks — deep links verificados only

This skill covers ONLY verified App Links. It does NOT cover nav destinations (`navigation-pattern`), Retrofit hosts (`api-client-auth`), o release signing (`play-store-release`).

## 1. Checklist accionable

- `assetlinks.json` en backend `new-api` sirviendo `https://miteve.vercel.app/.well-known/assetlinks.json` con la huella release (NO debug):
  ```json
  [{"relation": ["delegate_permission/common.handle_all_urls"],
    "target": {"namespace": "android_app", "package_name": "app.vercel.miteve.twa",
               "sha256_cert_fingerprints": ["AA:BB:CC:..."]}}]
  ```
- `intent-filter` con `autoVerify` en AMBOS launchers (`MobileActivity` + `TvActivity`), mismo `applicationId`:
  ```xml
  <!-- app/src/main/AndroidManifest.xml -->
  <activity android:name="app.miteve.presentation.mobile.MobileActivity" android:exported="true">
      <intent-filter android:autoVerify="true">
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data android:scheme="https" android:host="miteve.vercel.app" android:pathPrefix="/player" />
      </intent-filter>
  </activity>
  ```
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/navigation/MiteveNavGraph.kt
  navDeepLink { uriPattern = "https://miteve.vercel.app/player/{contentId}" }
  // ViewModel: contentId -> PlayerViewModel.load(contentId) (misma UiState phone/TV)
  ```

## 2. Naming

- Rutas: `https://miteve.vercel.app/player/{contentId}`, `https://miteve.vercel.app/details/{contentId}`. Sin query params propietarios; `?offline=true` solo para player offline (`player-offline`).
- Test: `adb shell am start -a android.intent.action.VIEW -d "https://miteve.vercel.app/player/42"` debe abrir player directo, sin chooser.

## 3. Verificación

- [ ] `https://digitalassetlinks.googleapis.com/v1/statements:list?source.web.site=https://miteve.vercel.app&relation=delegate_permission/common.handle_all_urls` devuelve el `package_name` + huella.
- [ ] `adb shell pm get-app-links app.vercel.miteve.twa` → `verified` (no `ask`/`none`).
- [ ] Paridad TWA: `asset_statements` del manifest web apunta al mismo package + huella (`play-store-release` SHA-256 coincide).
- [ ] Deep-link frío (app muerta) y tibio (app viva) abren `player/{id}` en phone Y TV.

## Out of scope

- Do NOT definir destinos/guards aquí. See `navigation-pattern`.
- Do NOT abrir contenido web en WebView — PROHIBIDO. Única excepción: OAuth vía Custom Tabs (fallback solo login OAuth, nunca player/details/home).
- Do NOT rotar huellas aquí. See `conn-secrets-android` + `play-store-release`.
