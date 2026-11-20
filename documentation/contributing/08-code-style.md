# Estilo de Código

> Convenciones Kotlin + Compose para MiTeVe-APK (single APK, phone + TV).

---

## Reglas base

- **ktfmt** como formateador (ver skill `kotlin-format-android`): import order, naming, sin wildcard salvo Compose.
- Tipos explícitos en APIs públicas; `val` por defecto; funciones pequeñas de una sola responsabilidad.
- Código y comentarios en **inglés**; docs e issues en **español**.
- Sin `!!` salvo justificado; prefiere `Result<T>` + `ErrorHandler` con Snackbar (ver `error-handling-android`).

---

## Estructura Compose (ver `compose-structure`)

Toda Screen sigue **ruta → estado → ViewModel → contenido**:

```kotlin
// Route + UiState + Intent en feature/downloads/
data class DownloadsUiState(
  val items: List<DownloadItem> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null
)

sealed interface DownloadsIntent {
  data object Retry : DownloadsIntent
  data class Pause(val id: String) : DownloadsIntent
}
```

```kotlin
@Composable
fun DownloadsRoute(
  viewModel: DownloadsViewModel = hiltViewModel(),
  onOpenItem: (String) -> Unit
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  DownloadsScreen(state = state, onIntent = viewModel::onIntent)
}
```

- La **Screen** solo renderiza `UiState`; el **ViewModel** posee el estado y los `Intent`.
- El mismo `ViewModel` + `UiState` se comparte entre `DownloadsScreen` (Mobile, touch)
  y `TvDownloadsScreen` (TV, D-pad). Solo cambia la capa de presentación.

---

## Theming (ver `compose-theming`)

- Material3 dark theme paridad con miteve-web (`#141414`), tokens centralizados en `core/ui/theme/`.
- Nada de colores hardcodeados en features; usar `MaterialTheme.colorScheme` + tokens.

---

## TV (ver `compose-tv-pattern`)

- `androidx.tv` foundation/material; foco siempre visible (`onFocusChanged` + borde/escala).
- Navegación 100% D-pad: verificar arriba/abajo/izquierda/derecha + OK + Back en cada pantalla.
- Respetar **overscan** (márgenes seguros ~5% / 27dp) y banner `android:banner` en `TvActivity`.
- Ejemplo: `TvPlayerScreen` usa los mismos controles que `PlayerScreen` pero con
  `TvButton` enfocables y `focusRequester` inicial en Play/Pause.

---

## Arquitectura (ver `player-offline`, `room-sync`)

```text
presentation (Mobile + TV) ──▶ domain ◀── data
```

- `presentation/`: Activities (`MobileActivity`, `TvActivity`), nav graphs, Screens, ViewModels.
- `domain/`: casos de uso + modelos (sin dependencias Android).
- `data/`: Retrofit (`BuildConfig.API_HOST`), Room, DataStore, Media3, WorkManager.

Ejemplo offline (`player-offline` + `room-sync` + `download-manager-ux`):

```kotlin
// domain: StartDownloadUseCase → data: DownloadRepository (Room + WorkManager + Media3)
class StartDownloadUseCase @Inject constructor(
  private val repository: DownloadRepository
) {
  suspend operator fun invoke(mediaId: String): Result<Unit> =
    repository.enqueueDownload(mediaId, requireUnmetered = true, requireCharging = true)
}
```

---

## Checklist antes de commitear

- [ ] ktfmt aplicado, sin warnings nuevos
- [ ] Paridad phone/TV verificada (o justificación según `parity-phone-tv`)
- [ ] Foco D-pad + overscan OK en TV
- [ ] `./gradlew app:assembleDevDebug` en verde
