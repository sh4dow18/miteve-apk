---
name: firebase-messaging
description: Use when wiring FCM new-episode pushes in miteve-apk — replaces TWA enableNotifications, genre topics, deep-link to player, Android 13 plus permission, no PII.
license: MIT
compatibility: opencode
---

# Firebase Messaging — nuevos episodios only

This skill covers ONLY FCM. It does NOT cover permiso UX genérica (`permissions-notifications`), crash/analytics (`crashlytics-analytics`), o App Links verification (`applinks-assetlinks`).

## 1. Checklist accionable (reemplaza `enableNotifications` TWA)

- Web TWA `enableNotifications` queda DEPRECADO: la suscripción vive en DataStore nativo (`notify_<genreId>: Boolean`), nunca en `localStorage` WebView.
- `MiteveMessagingService` + topics por género:
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/messaging/MiteveMessagingService.kt
  class MiteveMessagingService : FirebaseMessagingService() {
      override fun onMessageReceived(msg: RemoteMessage) {
          val id = msg.data["contentId"] ?: return
          notify("Nuevo episodio", msg.data["title"], deepLink("https://miteve.vercel.app/player/$id"))
      }
      override fun onNewToken(token: String) { /* enviar a new-api POST /api/push/tokens (sin email) */ }
  }
  ```
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/messaging/TopicManager.kt
  fun genreTopic(genreId: String) = "genre_$genreId" // subscribe/unsubscribe al togglear en ajustes
  ```

## 2. Naming

- Topics: `genre_<slug>` (`genre_anime`, `genre_drama`), más `new_season`. Canal: `MIT_EVE_EPISODES` ("Nuevos episodios", importancia default).
- Payload new-api: `{ contentId, title, genreId }` — prohibido email, nombre real, o historial en el payload.

## 3. Orden

1. `google-services.json` por flavor (gitignored si contiene secretos — ver `conn-secrets-android`).
2. `MiteveMessagingService` + canal + deep-link a `player/{id}`.
3. Toggle por género en ajustes → `subscribeToTopic` + permiso Android 13+ justo-a-tiempo (`permissions-notifications`).
4. Test: consola FCM → push de prueba en phone y TV (TV muestra low-priority).

## Verificación

- [ ] `onMessageReceived` con app en foreground muestra notificación + abre `player/{id}` al tap.
- [ ] Sin PII en token registration ni payload (auditar con Charles/logcat).
- [ ] Denegar `POST_NOTIFICATIONS` no crashea; toggle queda pendiente.
- [ ] TV: notificación no interrumpe playback ( heads-up suprimido durante `PlayerTvScreen`).

## Out of scope

- Do NOT definir rationale genérica aquí. See `permissions-notifications`.
- Do NOT loggear pushes como analytics con userId aquí. See `crashlytics-analytics`.
- Do NOT abrir pushes en WebView — prohibido salvo OAuth/Custom Tabs.
