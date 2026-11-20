# Primeros Pasos

> Cómo clonar, configurar y ejecutar MiTeVe-APK nativa por primera vez.

---

## Clonar el repositorio

```bash
git clone https://github.com/sh4dow18/miteve-apk.git
cd miteve-apk
git checkout develop
```

---

## Abrir en Android Studio

1. Abrir Android Studio → **Open** → carpeta `miteve-apk`.
2. Dejar que Gradle sincronice (AGP 8.9.1, `compileSdk 36`).
3. Verificar que existan los módulos y los launchers:
   - `MobileActivity` (launcher phone: `LAUNCHER`, sin `LEANBACK_LAUNCHER`)
   - `TvActivity` (launcher TV: `LEANBACK_LAUNCHER` + `android:banner`)

---

## Flavors dev / prod

El proyecto usa dos flavors (ver `app/build.gradle`):

| Flavor | `applicationIdSuffix` | `BuildConfig.API_HOST` | Uso |
| ------ | --------------------- | ---------------------- | --- |
| `dev` | `.dev` | `https://dev-api.miteve.vercel.app` | Desarrollo diario, QA |
| `prod` | (ninguno) | `https://api.miteve.vercel.app` | Release / Play Store |

Comandos:

```bash
# Compilar variante dev (la de todos los días)
./gradlew app:assembleDevDebug

# Instalar en emulador phone
./gradlew app:installDevDebug

# Compilar release prod (firmada, ver keystore abajo)
./gradlew app:assembleProdRelease
./gradlew app:bundleProdRelease
```

> Todos los ejemplos de esta guía usan `devDebug` salvo que se indique lo contrario.

---

## `local.properties` y keystore (fix)

**Nunca commitear `local.properties` ni el keystore.** Si el release falla con
`Keystore file not set` o `signingConfig`, aplicar este fix:

```properties
# local.properties (NO commitear)
sdk.dir=C\:\\Users\\tu-usuario\\AppData\\Local\\Android\\Sdk
storeFile=C\:/Users/tu-usuario/keystores/miteve-release.jks
storePassword=****
keyAlias=miteve
keyPassword=****
```

1. Crear el keystore una sola vez:
   ```bash
   keytool -genkeypair -v -keystore miteve-release.jks -alias miteve -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Guardarlo **fuera del repo** y apuntar `storeFile` a esa ruta.
3. `app/build.gradle` lee estas propiedades solo en `prodRelease`; `devDebug` firma con la clave debug automática.

Ver [Releases](11-releases.md) para el flujo Play Store completo.

---

## Ejecutar phone + TV

1. Levantar el emulador **phone** → Run `app` (variante `devDebug`) → abre `MobileActivity`.
2. Levantar el emulador **TV** → Run `app` (variante `devDebug`) → abre `TvActivity` (Leanback).
3. Probar login contra el backend `dev` y navegar con touch (phone) y D-pad (TV).

---

## Verificar que funciona

1. La app abre sin crash en ambos emuladores.
2. El player reproduce con Media3/ExoPlayer (ver `player-offline`).
3. No hay errores en Logcat (filtro `MiTeVe`).
4. `./gradlew app:assembleDevDebug` termina en verde.
