# Conocer Más

> Fin de la información general. Documentación técnica avanzada de MiTeVe-APK.

Si llegaste hasta aquí ya revisaste [CONTRIBUTING.md](../../CONTRIBUTING.md) y las guías de
[contributing](.). Ahora sigue la documentación técnica por área.

---

## Documentación técnica por objetivo

| Área | Temas |
| ---- | ----- |
| Player | `player-offline`: Media3 ExoPlayer DASH, Mobile touch + PiP vs TV D-pad |
| Downloads | `download-manager-ux`: lista, progreso, pausa, cuotas, WiFi + charging |
| Auth | `api-client-auth` (JWT, `BuildConfig.API_HOST`), `biometric-auth` (perfiles adultos) |
| Navigation | `navigation-pattern`: nav graphs Mobile/TV, deep links, guards |
| Sync | `room-sync`: Room + WorkManager contra new-api |
| TV-Focus | `compose-tv-pattern`: foco, overscan, Leanback, banner |
| Performance | `baseline-performance`: R8, Baseline Profiles, arranque < 2s |
| Release | `play-store-release` + `version-tag`: tracks, firma, versionado |
| General | `error-handling-android`, `crashlytics-analytics`, `firebase-messaging`, `permissions-notifications`, `cast-connect`, `applinks-assetlinks` |

---

## Estructura de carpetas (objetivo)

```text
app/src/main/java/app/miteve/
├── mobile/            # MobileActivity + nav graph + Screens phone
├── tv/                # TvActivity (LEANBACK) + nav graph + Screens TV
├── feature/
│   ├── player/        # PlayerScreen + TvPlayerScreen + PlayerViewModel (UiState compartido)
│   ├── downloads/     # DownloadsScreen + TvDownloadsScreen + DownloadManager
│   ├── auth/
│   └── ...
├── domain/            # Use cases + modelos (sin Android)
├── data/              # Retrofit (API_HOST), Room, DataStore, Media3, WorkManager
└── core/ui/theme/     # Tokens Material3 dark #141414
```

---

## Agregar nueva documentación

1. Crear el archivo en `documentation/learn-more/...` según el área.
2. Actualizar la tabla de este archivo.
3. Actualizar el enlace en `CONTRIBUTING.md` si aplica.
