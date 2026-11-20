# Contributing a MiTeVe-APK

> Guía de referencia para el equipo de MiTeVe-APK nativa.

---

## Qué es MiTeVe-APK

**MiTeVe-APK** es la app nativa de MiTeVe para Android (**Kotlin + Compose + Compose TV**),
distribuida como **single APK con dual launcher phone/TV**:

| Pieza | Rol |
| ----- | --- |
| `MobileActivity` | Launcher phone (`LAUNCHER`): touch, PiP, descargas, notificaciones |
| `TvActivity` | Launcher TV (`LEANBACK_LAUNCHER` + `android:banner`): D-pad, foco visible, overscan |
| ViewModels + `UiState` compartidos | Misma lógica y estado en ambas gemelas (ej: `PlayerUiState`, `DownloadsUiState`) |
| Backend `new-api` | API REST (`BuildConfig.API_HOST`: dev `https://dev-api.miteve.vercel.app`, prod `https://api.miteve.vercel.app`) |

Cada feature existe en **gemelas phone + TV** con el mismo estado (ver `parity-phone-tv`).
Ejemplo: `PlayerScreen` (Mobile) y `TvPlayerScreen` (TV) consumen el mismo `PlayerViewModel`
con Media3/ExoPlayer + Room + WorkManager (ver `player-offline`).

---

## Arquitectura del proyecto

```text
presentation (Mobile + TV) ──▶ domain ◀── data
```

| Capa | Contenido | Responsabilidad |
| ---- | --------- | --------------- |
| `presentation/` | `MobileActivity`, `TvActivity`, nav graphs, Screens, ViewModels | UI phone + TV, `UiState`/`Intent` |
| `domain/` | Use cases, modelos, repositorios (interfaces) | Lógica de negocio sin Android |
| `data/` | Retrofit, Room, DataStore, Media3, WorkManager | Red, caché offline, descargas |

Regla de dependencias: `presentation → domain ← data`. La UI nunca habla con red/BD
directo: todo pasa por casos de uso + repositorios.

---

## Propósito de esta guía

- Estandarizar el desarrollo phone + TV.
- Facilitar el onboarding (qué instalar, cómo compilar `devDebug`, cómo probar en ambos emuladores).
- Mantener calidad (checks CI, convenciones, paridad phone/TV en cada PR).

---

## A quién va dirigido

- **Equipo actual**: referencia rápida de procesos y convenciones.
- **Nuevas incorporaciones**: onboarding (leer en orden 01 → 02 → 04).
- **Colaboradores externos**: cómo contribuir sin romper paridad ni firma.

---

## Cómo usar esta guía

### Para nuevos miembros

1. [Requisitos](documentation/contributing/01-requirements.md) — Android Studio + JDK 17 + SDK 36 + emuladores phone/TV.
2. [Primeros Pasos](documentation/contributing/02-getting-started.md) — clonar, flavors dev/prod, `local.properties` + keystore fix, `assembleDevDebug`.
3. [Flujo de Trabajo](documentation/contributing/04-workflow.md) — issue → branch → phone+TV → PR → QA apk → squash.

### Para miembros actuales

- [Estrategia de Ramas](documentation/contributing/05-branching.md) · [Commits](documentation/contributing/06-commits.md) · [Pull Requests](documentation/contributing/07-pull-requests.md) (formato Observaciones + matriz phone/TV).
- [Uso de IA](documentation/contributing/20-ai.md) · [Preguntas Frecuentes](documentation/contributing/21-faq.md).

---

## Tabla de Contenidos

| # | Sección | Descripción |
| - | ------- | ----------- |
| 1 | [Requisitos](documentation/contributing/01-requirements.md) | Android Studio, JDK 17, SDK 36, emuladores phone/TV |
| 2 | [Primeros Pasos](documentation/contributing/02-getting-started.md) | Clonar, flavors dev/prod, keystore fix, `assembleDevDebug` |
| 4 | [Flujo de Trabajo](documentation/contributing/04-workflow.md) | Issue → branch → phone+TV → PR → QA apk → squash (release con screenshots Leanback) |
| 5 | [Estrategia de Ramas](documentation/contributing/05-branching.md) | Naming `Tipo/<issue>-desc` |
| 6 | [Convenciones de Commits](documentation/contributing/06-commits.md) | `#<issue> <tipo>:` en inglés |
| 7 | [Pull Requests](documentation/contributing/07-pull-requests.md) | Observaciones + matriz phone/TV |
| 8 | [Estilo de Código](documentation/contributing/08-code-style.md) | ktfmt, compose-structure, theming `#141414` |
| 10 | [Control de Versiones](documentation/contributing/10-versioning.md) | versionCode/Name en sync con web+api |
| 11 | [Releases](documentation/contributing/11-releases.md) | Play tracks + banner TV |
| 19 | [Configuración de GitHub](documentation/contributing/19-github-config.md) | Mapa `.github/` + Project MiTeVe-APK con Auto-add (como VIMAX 18) |
| 20 | [Uso de IA](documentation/contributing/20-ai.md) | Prompts + skills |
| 21 | [Preguntas Frecuentes](documentation/contributing/21-faq.md) | Soluciones comunes |
| 22 | [Conocer Más](documentation/contributing/22-learn-more.md) | Índice técnico por objetivo |
