# Flujo de Trabajo

> Ciclo completo de desarrollo en MiTeVe-APK nativa: issue → branch → phone+TV → PR → QA apk → squash.

---

## Diagrama del flujo de desarrollo

```text
┌────────┐    ┌────────┐    ┌─────────────┐    ┌────────┐    ┌────────┐    ┌────────┐    ┌────────┐    ┌────────┐
│ Issue  │───▶│ Branch │───▶│ Code phone  │───▶│ Commit │───▶│ Push   │───▶│ PR     │───▶│ Review │───▶│develop │
│(tarea) │    │(crear) │    │ + TV gemela │    │(guardar)│   │(subir) │   │(crear) │   │(revisar)│   │(actual)│
└────────┘    └────────┘    └─────────────┘    └────────┘    └────────┘    └────────┘    └────────┘    └────────┘
                                                        │ QA apk en emuladores │
                                                        └─────────────────────┘
```

---

## Diagrama del flujo de release

```text
                      develop
                         │
                         │ (cada 2-3 semanas)
                         ▼
                     PR a main
                         │
                         ▼
              ┌─────────────────────┐
              │ QA apk devDebug en  │
              │ emuladores phone+TV │
              └─────────┬───────────┘
                         │
          ┌──────────────┴──────────────┐
          │                             │
          ▼                             ▼
    ┌───────────┐                ┌────────────┐
    │ Aprobado  │                │ No aprobado│
    └─────┬─────┘                └─────┬──────┘
          │                             │
          ▼                             │
 ┌─────────────────┐                    │
 │ release/X.Y.Z   │                    │
 └────────┬────────┘                    │
          │                             │
          ▼                             │
    ┌───────────┐                       │
    │  Release  │                       │
    └───────────┘                       │
                                        │
           ┌────────────────────────────┘
           │
           ▼
  Volver a develop para corregir
  y repetir el ciclo
```

Release = `bundleProdRelease` firmado + screenshots **phone y TV (con banner Leanback)**.
Ver [Releases](11-releases.md).

---

## Flujo de hotfix

Si hay error en producción:

```text
Si hay error en producción:
        │
        ├─── Release NO es el último ──▶ Crear rama desde release/X.Y.Z
        │
        └─── Release ES el último ─────▶ Crear rama desde main
                                              │
                                              ▼
                                     ┌─────────────────┐
                                     │ hotfix/X.Y.(Z+1)│
                                     └────────┬────────┘
                                              │
                                              ▼
                                     ┌──────────────┐
                                     │ QA apk aprueba│
                                     └──────┬───────┘
                                            │
                                            ▼
                                   ┌─────────────────┐
                                   │ release/X.Y.(Z+1)│
                                   └─────────────────┘
```

---

## Pasos detallados

### 1. Issue

Todo trabajo comienza con un issue (Bug, Solicitud o Tarea). Indica siempre:

- **Objetivo**: Player, Downloads, Auth, Navigation, Sync, TV-Focus, Performance, Release, General.
- **Form-factor**: Phone, TV o Both.
- En features: **phone-twin + tv-twin** (o justificación según parity-phone-tv).

### 2. Branch

```bash
git checkout develop
git pull origin develop
git checkout -b Feature/123-player-offline-downloads
```

Ver [Estrategia de Ramas](05-branching.md).

### 3. Code (phone + TV)

Desarrollar la feature en **ambas gemelas** con el mismo `UiState` compartido:

- Phone: `MobileActivity` → nav graph Mobile → `PlayerScreen` (touch, PiP).
- TV: `TvActivity` → nav graph TV → `TvPlayerScreen` (D-pad, foco visible, overscan).
- Estado: `PlayerUiState` en el ViewModel compartido; la capa `data` expone Media3 + Room + WorkManager (ver `player-offline`).

Probar en **ambos emuladores** antes de commitear.

### 4. Commit

```bash
git add .
git commit -m "#123 feat: add offline downloads with media3"
```

Ver [Convenciones de Commits](06-commits.md).

### 5. Push

```bash
git push origin Feature/123-player-offline-downloads
```

### 6. Pull Request

Crear PR contra `develop` desde GitHub:

- **Título**: nombre exacto de la rama.
- **Descripción**: plantilla completa (Historia, Descripción, Cambios, Cómo probar, Criterios, Capturas **+ sección `Phone vs TV`**).

Ver [Pull Requests](07-pull-requests.md).

### 7. Review

- Checks automáticos en verde (CI assemble + lint, commitlint, branch-lint).
- Observaciones con formato **Observaciones + matriz phone/TV** (ver [Pull Requests](07-pull-requests.md)).
- `auto-assign.yml` asigna reviewer (`sh4dow18`) y hereda labels + form-factor del issue.

### 8. QA apk + Merge

1. QA instala el APK `devDebug` en emuladores phone + TV.
2. Valida la matriz phone/TV del PR (incluye foco D-pad y overscan en TV).
3. Tras aprobación → **Squash and Merge** a `develop` → eliminar la rama.

---

## Ramas principales

| Rama | Propósito |
| ---- | --------- |
| `main` | Última versión estable aprobada por QA |
| `develop` | Base de desarrollo activo |
| `Feature/*` | Nuevas funcionalidades (phone + TV) |
| `Fix/*` | Corrección de bugs |
| `Refactor/*` | Refactorización |
| `Docs/*` | Solo documentación |
| `release/*` | Release de producción (trazabilidad) |
| `hotfix/*` | Corrección urgente en producción |

---

## Flujo rápido (resumen)

```bash
# 1. Actualizar develop
git checkout develop && git pull

# 2. Crear rama
git checkout -b Feature/123-player-offline-downloads

# 3. Desarrollar phone + TV y probar en ambos emuladores

# 4. Commitear y subir
git add . && git commit -m "#123 feat: add offline downloads with media3"
git push origin Feature/123-player-offline-downloads

# 5. Crear PR en GitHub contra develop (con sección Phone vs TV)

# 6. QA apk → squash merge → eliminar rama
git branch -d Feature/123-player-offline-downloads
```
