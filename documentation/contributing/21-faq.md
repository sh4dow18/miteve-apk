# Preguntas Frecuentes

> Problemas comunes y soluciones en MiTeVe-APK nativa.

---

## Build y Gradle

### `assembleDevDebug` falla: "SDK location not found"

Crear `local.properties` (no commitear) con:

```properties
sdk.dir=C\:\\Users\\tu-usuario\\AppData\\Local\\Android\\Sdk
```

### `Keystore file not set` en release

Agregar a `local.properties` `storeFile/storePassword/keyAlias/keyPassword`
(ver [Primeros Pasos](02-getting-started.md)). `devDebug` no necesita keystore.

### Error: "compileSdk 36 not installed"

Instalar API 36 + build-tools 36.0.0 desde SDK Manager (o deja que CI lo instale).

---

## Emuladores phone / TV

### No veo `TvActivity` en el emulador phone (o viceversa)

Es lo esperado: `MobileActivity` solo aparece en launchers phone, `TvActivity`
(sola con `LEANBACK_LAUNCHER`) solo en Android TV. Usa el emulador correspondiente.

### El foco se pierde en TV

Verificar: cada pantalla TV debe tener un `focusRequester` inicial y orden de foco
lógico con D-pad. Probar arriba/abajo/izq/der + OK + Back. Ver `compose-tv-pattern`.

### La UI TV se recorta en los bordes

Respetar overscan (márgenes seguros). Verificar `android:banner` en `TvActivity`.

---

## Player y descargas

### ExoPlayer no reproduce offline

Verificar: descarga completada en Room (`Download` con estado DONE), archivo en
almacenamiento interno/app-specific y red apagada. Ver `player-offline`.

### WorkManager descarga con datos móviles

Los constraints deben ser `requireUnmetered + requireCharging`. Ver `download-manager-ux`.

---

## Backend / Auth

### `401` contra `BuildConfig.API_HOST`

Verificar flavor (`dev` → `https://dev-api.miteve.vercel.app`) y refresh del JWT.
Ver `api-client-auth`. Nunca hardcodear hosts: solo `BuildConfig.API_HOST`.

---

## Git y PRs

### Commit rechazado por formato

Usar `#<issue> <tipo>: <desc>` en inglés (ej: `#123 feat: add offline downloads`).
El `#` debe coincidir con la rama. Ver [Commits](06-commits.md).

### PR sin sección Phone vs TV

Completarla con screenshots reales phone + TV y el comando
`./gradlew app:assembleDevDebug` en verde. Ver [Pull Requests](07-pull-requests.md).

### CI rojo en `connectedDevDebugAndroidTest`

Ese paso es tolerante (`continue-on-error`): si no hay emulador en CI avisa con
warning y no bloquea, pero debe quedar registrado en el PR. El resto (assemble, lint,
commitlint, branch-lint) sí bloquea.

---

## Versiones y releases

### Play rechaza el AAB (`version code already used`)

Subir `versionCode` (+1) en `app/build.gradle` y alinear `versionName` con web+api.
Ver [Versioning](10-versioning.md).

### ¿Dónde va el banner TV?

`android:banner` en el `TvActivity` del `AndroidManifest.xml` + screenshots TV en la
ficha de Play. Ver [Releases](11-releases.md).

---

## Dónde buscar ayuda

| Recurso | Dónde |
| ------- | ----- |
| Issues | https://github.com/sh4dow18/miteve-apk/issues |
| Guía | [CONTRIBUTING.md](../../CONTRIBUTING.md) |
| Board | Project `MiTeVe-APK` (Auto-add) |
| Docs Android | https://developer.android.com |
| Docs Compose TV | https://developer.android.com/tv/compose |
