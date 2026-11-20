---
name: cast-connect
description: Use when adding phone-to-TV casting in miteve-apk — MediaRouter plus Cast SDK, remote queue, play/pause/seek, same UiState as player-offline, graceful disconnect.
license: MIT
compatibility: opencode
---

# Cast Connect — phone→TV remoto only

This skill covers ONLY Cast. It does NOT cover local playback (`player-offline`), download UI (`download-manager-ux`), o analytics de cast (`crashlytics-analytics`).

## 1. Checklist accionable

- Dependencia Cast SDK + `MediaRouter` solo en flavor phone (TV nunca castea a sí misma):
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/cast/CastSessionHolder.kt
  class CastSessionHolder @Inject constructor(@ApplicationContext ctx: Context) {
      val sessionManager: SessionManager = CastContext.getSharedInstance(ctx).sessionManager
      fun remoteSeek(positionMs: Long) { (sessionManager.currentCastSession?.remoteMediaClient)?.seek(positionMs) }
  }
  ```
- Misma `UiState`: `PlayerViewModel` expone `positionMs/isPlaying/track`; la UI remota (`CastMiniController` + pantalla expandida) consume el MISMO estado que `PlayerMobileScreen` — solo cambia el sink (local `ExoPlayer` vs `RemoteMediaClient`). Cola: `MediaQueueItem[]` con episodios siguientes.
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/cast/CastMiniController.kt
  @Composable fun CastMiniController(state: PlayerUiState, onIntent: (PlayerIntent) -> Unit) {
      // Play/Pause/Seek delegan a PlayerIntent; el Holder decide local vs remoto
  }
  ```

## 2. Naming

- Clases en `app/src/main/kotlin/app/miteve/presentation/feature/cast/`: `CastSessionHolder.kt`, `CastMiniController.kt`, `CastExpandedControls.kt`.
- Intents reutilizados: `PlayerIntent.Play/Pause/SeekTo/Next` — prohibido crear `CastIntent` duplicado.

## 3. Orden

1. `CastSessionHolder` + botón cast en `PlayerMobileScreen` (solo si hay ruta disponible).
2. Mini-controller persistente + expandida con cola.
3. Disconnect elegante (sección 4) + test (`apk-testing`: fake `RemoteMediaClient`).

## 4. Disconnect elegante (obligatorio)

- Al desconectar: retomar localmente en la MISMA `positionMs` (`player.playWhenReady=true; seekTo(lastRemotePosition)`), toast ES "Vuelves al teléfono — minuto 12:04". Nunca cortar en negro ni reiniciar desde 0.
- Si el receptor muere: `onSessionEnded` → fallback local automático + Snackbar reintentar.

## Verificación

- [ ] Mismo `PlayerViewModel` test Turbine pasa con sink local y remoto.
- [ ] Cola avanza al siguiente episodio sin intervención.
- [ ] Disconnect retoma local ±2s de la posición remota.

## Out of scope

- Do NOT definir DASH local aquí. See `player-offline`.
- Do NOT pedir permisos aquí. See `permissions-notifications`.
- Do NOT renderizar receptor en WebView — prohibido salvo OAuth/Custom Tabs.
