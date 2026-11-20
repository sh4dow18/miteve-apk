# Configuración de GitHub (.github/)

> Qué hay en `.github/`, para qué sirve cada archivo y cómo está configurado en MiTeVe-APK.

---

## Estructura

```text
.github/
├── pull_request_template.md      # Plantilla de PR (historia + Phone vs TV)
├── ISSUE_TEMPLATE/
│   ├── config.yml                # Bloquea blank issues + link a CONTRIBUTING
│   ├── bug_report.yml            # Reportar Error (label Bug + form-factor)
│   ├── feature_request.yml       # Solicitar Funcionalidad (label Solicitud + twins)
│   └── task.yml                  # Generar Tarea (sin label + form-factor)
└── workflows/
     ├── ci.yml                   # Robot: assembleDevDebug + tests + lint
     ├── commitlint.yml           # Robot: commits + título del PR
     ├── branch-lint.yml          # Robot: nombre de rama
     └── auto-assign.yml          # Robot: reviewer + assignee + labels/form-factor
```

---

## Plantilla de Pull Request

`.github/pull_request_template.md`: historia (`Como/Quiero/Para`), descripción, cambios,
cómo probar, criterios, capturas **+ sección `Phone vs TV`** obligatoria:

- Mobile PASS (screenshot) · TV PASS (screenshot + foco) · `assembleDevDebug` en verde.

Ver [Pull Requests](07-pull-requests.md).

---

## Plantillas de Issues

| Formulario | Label | Campos clave |
| ---------- | ----- | ------------ |
| Bug | `Bug` | descripción, pasos, esperado, actual, **objetivo**, **form-factor** (Phone/TV/Both, obligatorio), **versión** (versionCode/flavor dev), capturas phone/TV, notas de foco TV |
| Feature | `Solicitud` | historia Como/Quiero/Para, descripción, criterios, **phone-twin + tv-twin** (requeridos o justificación parity-phone-tv), form-factor, objetivo, mockups |
| Tarea | (sin label) | descripción, tareas, objetivo, form-factor |

**Objetivos**: Player, Downloads, Auth, Navigation, Sync, TV-Focus, Performance, Release, General.

---

## Workflows

### `ci.yml` — compila la APK

Triggers `push`/`pull_request` en `develop`/`main`: JDK 17 + Android SDK 36 →
`app:assembleDevDebug` → `app:connectedDevDebugAndroidTest` (**allow-failure** con aviso si
no hay emulador) → `app:lintDevDebug`.

### `commitlint.yml` — commits y título

Valida `#<issue> <tipo>:` + correlación rama↔commit + `Docs/*` solo `docs:` +
título PR = rama (excepto checkpoints `develop → main`, `main → release/*`).

### `branch-lint.yml` — nombre de rama

Regex `^(Feature|Fix|Refactor|Docs)/[0-9]+-[a-z0-9-]+$`, exentas `main`/`develop`/`release/*`/`hotfix/*`.

### `auto-assign.yml` — asignación automática

Al abrir PR (incluye draft): reviewer **`sh4dow18`** (salvo que sea el autor),
assignee = autor, y copia **labels + form-factor** del issue `Tipo/123-desc`
(el form-factor se detecta en el cuerpo del issue y se añade como `form-factor: Phone|TV|Both`).

---

## Project board (manual)

No hay workflow para el board: crear el Project **`MiTeVe-APK`** en GitHub y activar
**Auto-add** como en VIMAX 18 (Project → `...` → Workflows → Auto-add to project),
así los issues/PRs entran solos al tablero sin automatización en este repo.
