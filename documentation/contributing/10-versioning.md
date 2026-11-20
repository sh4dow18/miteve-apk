# Control de Versiones

> Cómo gestionar `versionCode`/`versionName` en MiTeVe-APK, en sync con miteve-web y miteve-api.

---

## Dónde vive la versión

| Campo | Archivo | Ejemplo |
| ----- | ------- | ------- |
| `versionCode` | `app/build.gradle` (`defaultConfig`) | `12` (siempre incremental) |
| `versionName` | `app/build.gradle` (`defaultConfig`) | `"1.2.0"` (SemVer) |

```gradle
defaultConfig {
    versionCode 12
    versionName "1.2.0"
}
```

---

## Reglas de sync (ver `version-tag`)

1. `versionName` de la APK = misma `X.Y.Z` que `package.json` (miteve-web) y la versión del backend (miteve-api/new-api).
2. `versionCode` **solo sube** (Play Store rechaza versionCodes repetidos o menores).
3. Cambiar versión **solo** en ramas `release/*` o `hotfix/*`, nunca en un Feature.
4. El tag se crea **solo desde GitHub UI** al publicar el release (`vX.Y.Z`), nunca local.

| Cambio | versionName | versionCode | Rama |
| ------ | ----------- | ----------- | ---- |
| Fix | `1.2.0` → `1.2.1` | `12` → `13` | `hotfix/1.2.1-...` |
| Feature | `1.2.1` → `1.3.0` | `13` → `14` | `release/1.3.0` |
| Breaking | `1.3.0` → `2.0.0` | `14` → `15` | `release/2.0.0` |

---

## Verificación

```bash
./gradlew app:assembleProdRelease
# El AAB/APK generado debe reportar el versionCode/versionName nuevos
```

---

## Errores comunes

| Error | Solución |
| ----- | -------- |
| Play rechaza la subida (`version code already used`) | Subir `versionCode` (+1) |
| `versionName` no coincide con web/api | Alinear las 3 antes del release |
| Tag creado local | Borrarlo y crearlo solo en GitHub UI al hacer el release |
