---
name: permissions-notifications
description: Use when requesting runtime permissions in miteve-apk — POST_NOTIFICATIONS, storage downloads, phone PiP, TV mic voice search, friendly Spanish rationale, never all upfront.
license: MIT
compatibility: opencode
---

# Permissions + Notifications — justo-a-tiempo only

This skill covers ONLY runtime permissions. It does NOT cover FCM topics (`firebase-messaging`), download Worker constraints (`download-manager-ux`), o Cast (`cast-connect`).

## 1. Regla de oro: nunca pedir todo al inicio

- Pedir en el momento del uso, con rationale ES amable UNA vez antes del diálogo del sistema. Si el usuario deniega 2 veces → deep-link a Ajustes, no re-preguntar en loop.
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/core/permissions/PermissionGate.kt
  @Composable
  fun PermissionGate(permission: String, rationale: String, onGranted: @Composable () -> Unit) {
      val launcher = rememberLauncherForActivityResult(RequestPermission()) { /* ... */ }
      // 1. if granted -> onGranted()  2. else show rationale card ES -> 3. launcher.launch(permission)
  }
  ```

## 2. Matriz (checklist accionable)

| Permiso | Cuándo | Ejemplo |
|---|---|---|
| `POST_NOTIFICATIONS` (Android 13+) | Al activar "Avisarme de nuevos episodios" en ajustes, NO en login | `PermissionGate(POST_NOTIFICATIONS, "Te avisamos solo de tus series seguidas.")` |
| Storage descargas | Al pulsar Descargar (ver `download-manager-ux`); Android 10+ usa scoped storage, sin `WRITE_EXTERNAL_STORAGE` | `DownloadButton(onClick = { gate -> gate(RequestType.Download) })` |
| PiP phone | Al salir del player con reproducción (`enterPictureInPictureMode`); declarar `supportsPictureInPicture=true` solo en `MobileActivity` | `app/src/main/kotlin/app/miteve/presentation/feature/player/PlayerMobileScreen.kt` |
| `RECORD_AUDIO` mic TV (voice search) | Al pulsar el icono 🎤 en search TV, NUNCA al abrir search | `app/src/main/kotlin/app/miteve/presentation/feature/search/SearchTvScreen.kt` |

## 3. Naming

- Textos rationale en `res/values/strings.xml`: `perm_notif_rationale`, `perm_dl_rationale`, `perm_mic_rationale` — tono ES amable ("tú", 1 línea, beneficio claro).
- Manifest: declarar solo lo usado; PiP solo phone, `RECORD_AUDIO` con `required="false"` para no filtrar TV sin mic:
  ```xml
  <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
  <uses-feature android:name="android.hardware.microphone" android:required="false" />
  ```

## 4. Verificación

- [ ] Fresh install → cero diálogos antes de la primera acción del usuario.
- [ ] Denegar → rationale no reaparece en loop; Ajustes accesible.
- [ ] TV sin mic: search por texto sigue funcional (mic degradado elegante).
- [ ] Phone PiP solo durante playback; TV sin PiP (Back pausa, ver `player-offline`).

## Out of scope

- Do NOT definir topics/deep-link FCM aquí. See `firebase-messaging`.
- Do NOT definir cuotas/SD aquí. See `download-manager-ux`.
- Do NOT usar WebView para pedir permisos — prohibido salvo OAuth/Custom Tabs.
