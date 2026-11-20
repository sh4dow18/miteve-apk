# Pull Requests

> Cómo crear, revisar y mergear PRs en MiTeVe-APK (formato Observaciones + matriz phone/TV).

---

## Crear un PR

Los PRs **siempre se crean desde la interfaz de GitHub**:

1. Push de tu rama al remoto.
2. GitHub → Pull requests → New pull request.
3. Base: `develop`; compare: tu rama.
4. Título = **nombre exacto de la rama** (ej: `Feature/123-player-offline-downloads`).
5. Descripción = plantilla completa (**no borrar secciones**).
6. Create pull request (vale en draft: `auto-assign` también corre).

---

## Descripción del PR

La descripción ya viene pre-llenada por `.github/pull_request_template.md`:

```markdown
# Pull Request

## Historia de Usuario

**Como** / **Quiero** / **Para**

## Descripción
## Cambios realizados
## Cómo probar
## Criterios de Aceptación
## Capturas (si aplica)

## Phone vs TV

- [ ] Mobile PASS (screenshot adjunto)
- [ ] TV PASS (screenshot + notas de foco D-pad, sin recortes por overscan)
- [ ] Comando automatizado: `./gradlew app:assembleDevDebug` en verde
```

| Sección | Obligatoria |
| ------- | ----------- |
| Historia de Usuario | Sí (`Como / Quiero / Para`) |
| Descripción | Sí |
| Cambios realizados | Sí (lista `- `) |
| Cómo probar | Sí (pasos `1. 2. 3.`, indicar emulador phone/TV) |
| Criterios de Aceptación | Sí (checklist `- [ ]`) |
| Capturas | Sí si toca UI (phone **y** TV) |
| Phone vs TV | Sí, siempre (los 3 checks) |

### Ejemplo

```markdown
# Pull Request

## Historia de Usuario

**Como** usuario con perfil adulto
**Quiero** descargar un capítulo para verlo offline
**Para** verlo sin conexión en phone y TV

## Descripción

Gestor de descargas con Media3 + WorkManager (solo WiFi + cargando), mismo
DownloadsUiState en Mobile y TV.

## Cambios realizados

- DownloadManager con WorkManager (constraints WiFi + charging)
- DownloadsScreen (Mobile, touch) + TvDownloadsScreen (TV, D-pad)
- Room: entidad Download + DAO con progreso

## Cómo probar

1. Emulador phone devDebug → Descargas → descargar capítulo con WiFi
2. Emulador TV devDebug → Descargas → navegar con D-pad, foco siempre visible
3. Apagar red → reproducir desde descargas (player offline)

## Criterios de Aceptación

- [ ] Descarga solo con WiFi + cargando
- [ ] Mismo UiState en phone y TV
- [ ] Foco D-pad nunca se pierde en TV

## Capturas (si aplica)

Screenshot phone + screenshot TV adjuntos.

## Phone vs TV

- [x] Mobile PASS (screenshot adjunto)
- [x] TV PASS (screenshot + foco verificado con D-pad)
- [x] Comando automatizado: `./gradlew app:assembleDevDebug` en verde
```

---

## Proceso de review

### 1. Checks automáticos

| Check | Workflow | Qué hace |
| ----- | -------- | -------- |
| CI | `ci.yml` | `assembleDevDebug` + `connectedDevDebugAndroidTest` (tolerante sin emulador) + `lintDevDebug` |
| Commitlint | `commitlint.yml` | Commits `#<issue> <tipo>:` + título PR = rama |
| Branch Lint | `branch-lint.yml` | Rama `Tipo/123-desc` |
| Auto Assign | `auto-assign.yml` | Reviewer `sh4dow18` + assignee + labels/form-factor del issue |

El PR no se mergea si algún check requerido falla.

### 2. Revisión de código

Al menos **1 aprobación**. El reviewer verifica calidad, convenciones
(`kotlin-format-android`/`compose-structure`), paridad phone/TV y ausencia de regresiones.

---

## Observaciones del PR

Cuando haya puntos que corregir, el reviewer deja **un solo comentario general** con el formato estándar. No observaciones dispersas ni solo inline.

### Formato obligatorio

```markdown
# Observaciones (DD de Mes del YYYY HH:MMam/pm)
Durante la revisión de los cambios realizados en esta Pull Request se identificaron los siguientes puntos que se deben atender.

## [Ubicación / Feature / Pantalla]

### [Categoría]

- [Descripción: qué se encontró, qué problema genera y qué se espera.]

## Matriz phone/TV

| Caso | Phone | TV | Notas |
| ---- | ----- | -- | ----- |
| Descargar con WiFi | ✅ PASS | ✅ PASS | ... |
| Foco D-pad en player | N/A | ❌ FAIL: el foco se pierde en Controles | Ver nota 1 |
| Overscan TvDownloads | N/A | ✅ PASS | ... |
```

Categorías más comunes: Funcionalidad, TV-Foco, Overscan/Diseño TV, Arquitectura
(`presentation → domain ← data`), Reglas de Negocio, Código, Performance, Traducciones.

### Reglas

1. Una observación por bullet, con los 3 elementos (encontrado → problema → esperado).
2. Ser específico: pantalla, pasos, captura o criterio que falla.
3. La **matriz phone/TV** es obligatoria si el PR toca UI.
4. Si no hay observaciones, no se deja el comentario: se aprueba directo.
5. Si hay observaciones, no se aprueba hasta re-validarlas (nuevo encabezado con fecha/hora).

---

## Mergear un PR

Método recomendado: **Squash and Merge** (historial limpio) → eliminar la rama:

```bash
git checkout develop
git pull origin develop
git branch -d Feature/123-player-offline-downloads
```

---

## Tamaño del PR

| Tamaño | Líneas | Ejemplo |
| ------ | ------ | ------- |
| Pequeño | 1-100 | Fix de foco TV |
| Mediano | 100-500 | Feature con gemela phone/TV |
| Grande | 500+ | Refactor de nav graph (evitar; dividir) |

---

## Errores comunes

| Error | Solución |
| ----- | -------- |
| PR contra rama incorrecta | Cerrar y reabrir contra `develop` |
| Falta sección Phone vs TV | Completarla con screenshots reales |
| Solo evidencia phone | Probar en emulador TV y adjuntar foco/overscan |
| Conflictos | Rebase con develop y push |
