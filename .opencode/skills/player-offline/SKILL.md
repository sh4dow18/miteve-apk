---
name: player-offline
description: Use when implementing streaming or downloads in miteve-apk with Media3 ExoPlayer DASH, Room+WorkManager offline, and Mobile touch vs TV D-pad players.
license: MIT
compatibility: opencode
---

# Player Offline — playback + downloads only

This skill covers ONLY playback and offline downloads. It does NOT cover Room schema detail (`room-sync`) or TV focus generics (`compose-tv-pattern`).

## Streaming (Media3, parity with web shaka-player)

- `ExoPlayer` + `DashMediaSource` for DASH, qualities `360p`–`1080p` (AV1 where api `/api/ffmpeg/*` provides it). Player holder lives in `PlayerViewModel`/dedicated `PlayerHolder` (lifecycle-aware), never inside a composable:
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/player/PlayerHolder.kt
  class PlayerHolder @Inject constructor(@ApplicationContext ctx: Context) {
      val player: ExoPlayer = ExoPlayer.Builder(ctx).build().apply {
          setMediaSource(DashMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(MediaItem.fromUri(dashUrl)))
          prepare()
      }
  }
  ```
- Track parity: web shaka ABR ↔ `DefaultTrackSelector` with max-height constraints per flavor/network. Report playback errors through `ApiResult` → Snackbar (see `error-handling-android`), never silent failure.
- PiP replaces web iOS-fullscreen behavior: phone player enters PiP on background during playback (`enterPictureInPictureMode`); TV has no PiP — D-pad Back pauses and returns to details.

## Offline (parity with web offline-db IndexedDB + sw.js + /downloads)

- Download = Room `Download` row + WorkManager `DownloadWorker` fetching segments + `OfflinePlayerScreen` playing local files. `player?offline=true` web semantics → native route `player/{contentId}?offline=true` rendering `OfflinePlayerScreen` from local URI.
- Twins: `PlayerMobileScreen` (touch gestures: double-tap seek, pinch-free) vs `PlayerTvScreen` (D-pad: left/right seek, center play/pause, visible focus on transport controls), both driven by the same `PlayerViewModel` state (position, buffering, tracks).

## Out of scope

- Do NOT define Room entities/sync cadence here. See `room-sync`.
- Do NOT define TV focus primitives here. See `compose-tv-pattern`.
- Do NOT stream inside a WebView — Media3 only. WebView ban applies.
