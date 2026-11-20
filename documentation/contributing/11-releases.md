# Releases

> Cómo cortar releases Play Store de MiTeVe-APK (tracks + banner TV).

---

## Cuándo

- Cada **2-3 semanas**: PR `develop → main` → QA apk → rama `release/X.Y.Z`.
- Hotfix: desde `main` (o `release/X.Y.Z` si no es el último) → `hotfix/X.Y.(Z+1)` → QA → `release/X.Y.(Z+1)`.

---

## Paso a paso

### 1. Congelar versión

En la rama `release/X.Y.Z`, alinear versión (ver [Versioning](10-versioning.md)):

```gradle
versionCode 14
versionName "1.3.0"
```

`versionName` en sync con miteve-web (`package.json`) y miteve-api.

### 2. Generar el bundle firmado

Con el keystore de `local.properties` configurado (ver [Primeros Pasos](02-getting-started.md)):

```bash
./gradlew app:bundleProdRelease
# Salida: app/build/outputs/bundle/prodRelease/app-prod-release.aab
```

### 3. QA del release

- Instalar `app-prod-release` (o `devDebug` equivalente) en **emulador phone + TV**.
- Checklist: login, player, descargas offline, D-pad + foco, overscan, banner Leanback en el launcher TV.
- Screenshots phone + TV para la ficha de Play Store.

### 4. Subir a Play Console (tracks)

| Track | Cuándo | Quién |
| ----- | ------ | ----- |
| **Internal** | Todo release nuevo (prueba del equipo) | Dev |
| **Closed** | QA externa / beta | QA |
| **Production** | Solo tras QA PASS en internal/closed | Release manager |

Subir el `.aab` al track correspondiente con notas del changelog.

### 5. Ficha de Store (phone + TV)

Obligatorio en cada release que toque UI:

- Screenshots **phone** (mínimo 2).
- Screenshots **TV** (mínimo 2) + verificación del **`android:banner`** en el launcher Leanback.
- Descripción en español con los cambios visibles.

### 6. Crear el release en GitHub

1. GitHub → Releases → **Draft a new release**.
2. Tag `vX.Y.Z` (solo desde la UI, target `release/X.Y.Z`).
3. **Generate release notes** → reorganizar por categorías (feat/fix/refactor/docs/chore).
4. Agregar fecha de publicación y tracks de Play alcanzados.
5. Adjuntar el `.aab` (opcional si ya está en Play) y publicar.

---

## Release Alpha (sin QA)

Si por calendario se sube sin QA completa:

- Rama `release/X.Y.Z-Alpha`, tag `vX.Y.Z-Alpha`.
- Solo al track **Internal**, indicado como Alpha en las notas.
- El siguiente release estable debe pasar QA completa.

---

## Notas

- **Siempre** crear el release en GitHub **antes** de promover a Production.
- El tag debe coincidir con `versionName`; el `versionCode` debe ser mayor que el anterior.
- **Nunca** crear tags desde la línea de comandos.
- El keystore y `local.properties` **nunca** se commitean.
