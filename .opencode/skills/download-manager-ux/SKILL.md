---
name: download-manager-ux
description: Use when building the downloads management UI in miteve-apk — extends player-offline and room-sync with list, progress, pause, delete, SD versus internal, quotas, WorkManager wifi-plus-charging constraints, retries.
license: MIT
compatibility: opencode
---

# Download Manager UX — gestión de descargas only

This skill covers ONLY la UI y política de gestión. Playback offline vive en `player-offline`, entidades/sync en `room-sync`.

## 1. Checklist accionable

- Pantallas gemelas `DownloadsMobileScreen` / `DownloadsTvScreen` (mismo `DownloadsViewModel`): lista + progreso + pausa/reanuda + borra + "ver offline":
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/downloads/DownloadsViewModel.kt
  data class DownloadUiState(val items: List<DownloadItem> = emptyList(), val quotaUsedBytes: Long = 0)
  sealed interface DownloadIntent { data class Pause(val id: String): DownloadIntent; data class Resume(val id: String): DownloadIntent; data class Delete(val id: String): DownloadIntent }
  ```
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/work/DownloadWorker.kt
  class DownloadWorker(ctx: Context, p: WorkerParameters) : CoroutineWorker(ctx, p) {
      override suspend fun doWork(): Result = try { /* segmentos DASH -> files */ Result.success() }
      catch (e: IOException) { if (runAttemptCount < 3) Result.retry() else Result.failure() }
  }
  // Constraints: wifi + charging + storageNotLow
  // Constraints.Builder().setRequiredNetworkType(UNMETERED).setRequiresCharging(true).setRequiresStorageNotLow(true).build()
  ```

## 2. SD vs interno + cuotas

- Selector destino en ajustes: interno (defecto) vs SD (`getExternalFilesDirs`); migrar NO mueve archivos existentes — avisar ES "las descargas actuales se quedan donde están".
- Cuota: 4GB por defecto, barra `quotaUsedBytes/cuota` en la cabecera; al superar → diálogo ES con "Borrar vistas" (bulk-delete `watched=true`), nunca borrar silencioso.

## 3. Naming

- `presentation/feature/downloads/Downloads{Mobile,Tv}Screen.kt` + `DownloadsViewModel.kt`; Worker en `data/work/DownloadWorker.kt`; entidad Room en `data/local/DownloadEntity.kt` (schema en `room-sync`).

## 4. Verificación

- [ ] Pausa/reanuda sobrevive a matar la app (WorkManager `ExistingWorkPolicy.KEEP`).
- [ ] Sin wifi o sin carga → worker `ENQUEUED`, badge "En espera de Wi-Fi".
- [ ] 3 reintentos con backoff exponencial, luego estado `Error` con Reintentar manual.
- [ ] TV D-pad: foco visible en progreso/acciones; phone: swipe-to-delete + undo Snackbar.

## Out of scope

- Do NOT definir entidades/sync aquí. See `room-sync`.
- Do NOT definir ExoPlayer local aquí. See `player-offline`.
- Do NOT pedir storage al inicio. See `permissions-notifications`.
- Do NOT listar descargas en WebView — prohibido salvo OAuth/Custom Tabs.
