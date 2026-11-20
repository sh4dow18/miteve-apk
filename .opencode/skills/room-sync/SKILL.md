---
name: room-sync
description: Use when defining Room entities/DAOs or syncing continue-watching, history, downloads, and profiles with miteve-api in miteve-apk.
license: MIT
compatibility: opencode
---

# Room Sync — local persistence + api sync only

This skill covers ONLY Room + DataStore schema and WorkManager sync against `new-api`. It does NOT cover playback (`player-offline`) or auth tokens (`api-client-auth`).

## Schema (`app/.../data/db/`)

- Entities: `ContentEntity` (catalog cache), `ContinueWatchingEntity(contentId, positionMs, updatedAt)`, `DownloadEntity(contentId, status, localPath, quality)`. Profiles/preferences (non-sensitive) in DataStore, never in Room as JSON blobs.
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/db/entity/ContinueWatchingEntity.kt
  @Entity(tableName = "continue_watching", primaryKeys = ["contentId", "profileId"])
  data class ContinueWatchingEntity(val contentId: String, val profileId: String, val positionMs: Long, val updatedAt: Long)
  ```
- DAO suspend functions returning `Flow` for observed rows; mappers in `data/mapper/` convert Entity↔domain. UI never imports `entity/*` or `dao/*` (see `android-clean-architecture`).

## Sync contract (new-api endpoints)

- Endpoints: `/continue-watching`, `/history`, `/downloads` (exact paths per api OpenAPI — verify against api repo before coding). Periodic `SyncWorker` (WorkManager, `ExistingPeriodicWorkPolicy.KEEP`, 6h + on-app-start expedited) pushes dirty rows then pulls server truth. Conflicts: server `updatedAt` wins for progress; client wins for new downloads until upload ack.
- Room is the single source of truth for reads: Screens collect `Flow` from Room; network refresh only writes into Room. No dual-state (network list vs db list) in ViewModels.

## Out of scope

- Do NOT define ExoPlayer behavior here. See `player-offline`.
- Do NOT store JWT/secrets in Room. See `api-client-auth`, `conn-secrets-android`.
- Do NOT define error UI here. See `error-handling-android`.
