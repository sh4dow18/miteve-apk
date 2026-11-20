---
name: apply-qa-apk
description: Use when doing strict exhaustive QA in miteve-apk on the current active branch with phone + Android TV emulators, full flows and adversarial cases, no code changes, and PR observations in project format with user-gated approve and merge.
license: MIT
compatibility: opencode
---

# Apply QA APK — Single Responsibility: probar la rama actual en teléfono + TV y reportar

This skill covers ONLY QA exhaustiva sobre la rama actual activa en emuladores phone + Android TV para cualquier feature. It does NOT edit code, Gradle config, migrations, or switch branches. READ-ONLY salvo builds/tests/emuladores/screenshots.

## Rules

- Este skill siempre aplica a la rama actual activa. Nunca pruebe otra rama a menos que el usuario lo especifique explícitamente con nombre de rama.
- Antes de cualquier prueba, identificar la PR asociada a la rama (`list_pull_requests` + `pull_request_read get`) y leer sus observaciones existentes (`get_comments` + `get_reviews` + `get_review_comments`). Si la PR ya tiene observaciones, el alcance de la pasada es SOLO el recheck de esas observaciones (más regresión mínima del flujo tocado en phone + TV); si no tiene observaciones, el alcance es todo lo del PR. En ambos casos, todo en una sola pasada exhaustiva: planificar la ruta completa al inicio (build → alcance PR → observaciones → credenciales → recorrido phone → recorrido TV → limpieza → reporte), no pedir confirmaciones intermedias salvo bloqueo real (credenciales, hardware, riesgo irreversible no autorizado).
- Prohibido dar reportes intermedios: aunque encuentre errores durante la pasada, no los reporte ni los anuncie en el chat hasta el final; anótelos en silencio y continúe con el recorrido. Solo al cerrar la pasada única entregue el reporte completo.
- En la revisión exigir build sin errores nuevos: `./gradlew app:assembleDevDebug` + `./gradlew app:installDevDebug` en verde antes del recorrido. Para emuladores/servidores de larga vida usar `timeout` grande o el shell los mata a los 120s. Shell Windows (PowerShell): no existe `wc` (usar `Measure-Object`), no hay heredoc `<<`.
- Identifique la PR conectada con `list_pull_requests` + `pull_request_read get/get_files`. Alcance = `git diff --name-only origin/develop...HEAD` más features del body. Dejar constancia de base/head/SHA y conteo de archivos.
- No hay usuario admin por defecto. Siempre pida al usuario los usuarios y contraseñas a usar en las pruebas antes de empezar, una sola vez al inicio de la pasada. No inventar credenciales ni saltar re-auth. Probar token ausente/expirado/rol indebido: sin crash, con mensaje amable + redirect login.
- Matriz obligatoria en cada pasada (mismo APK, 2 launchers):
  - Phone emulator (Pixel, API 35): cold start vía `MobileActivity` LAUNCHER, `installDevDebug`, navegar home → search → player → downloads → profile + admin guards. Touch + rotación + background/foreground + kill/relaunch.
  - Android TV emulator (1080p Leanback, API 35): mismo APK, cold start vía `TvActivity` LEANBACK_LAUNCHER. Navegar el flujo COMPLETO solo con flechas + Enter (sin mouse). Foco nunca desaparece, overscan 48dp respetado (ver `compose-tv-pattern`), texto legible 10-foot.
  - Automatizado: `./gradlew app:connectedDevDebugAndroidTest` (Espresso) y/o `maestro test` donde aplique. Registrar comando exacto + tail output.
- En entorno de pruebas usar datos propios `QA_*` (crear → probar → borrar si hay delete; si no hay delete, documentar rastro). Sobre fixtures compartidos sin restauración, no borrar: documentar como límite.
- Por cada feature probar flujo completo en phone Y en TV en la misma pasada: listar, paginación, filtros, validación vacía y límites, crear/leer/actualizar/borrar donde aplique, player DASH 360p/1080p AV1 (touch vs D-pad), descarga Room+WorkManager, `OfflinePlayerScreen`, PiP, rotación, airplane→offline, API down→mensaje amable. No omitir features por parecer solo lectura (400/404/409/503 se verifican igual). Chequear `parity-phone-tv`: cada MobileScreen tiene su TvScreen con mismo route + ViewModel + UiState.
- En cada revisión, actuar además como usuario malicioso: inputs inválidos, textos larguísimos, SQL/XSS en búsqueda, IDs inexistentes/negativos, offline intermitente, storage lleno, permisos denegados, kill durante descarga/transcode. Nada tumba la app: rechazo con mensaje amable + Snackbar, sin crash, sin `500` sin manejar, sin corrupción Room; lo creado a prueba eliminarlo y confirmar conteo base.
- Registre logcat `E`/`FATAL`, network `>=400` salvo `204`. Guarde evidencias en `qa/<date>/<feature>/phone.png` + `tv.png` + logs (git-ignored, adjuntar a PR/issue, nunca commitear a `main`) y anote IDs exactos (content.id, profileId, downloadId, versionCode, flavor dev). No dejar `QA_*` salvo autorizado.
- Documentar al final lo verificado OK en positivo y los límites no verificables (sin device físico TV/phone, sin DRM real, sin spam masivo, sin dañar fixtures) fuera del comentario PR, en el chat.
- Nunca edite el código. Es solo revisión de QA exhaustiva. Hallazgos van a `issue-bug`, no se fixean aquí.

## Reporte y cierre

- Reporte solo errores con: feature/form-factor (phone/TV), pasos, comportamiento encontrado, problema que genera, esperado. Incluya screenshots paths, focus notes TV, logs exactos, comando automatizado + resultado.
- Al cerrar la pasada única, entregar en el chat: build (errores/warnings), alcance aplicado (recheck o revisión completa phone+TV), lo verificado feature por feature, hallazgos numerados y límites no verificables. Después, entregar el texto exacto y completo del comentario PR en bloque copiable:
```markdown
## QA — <feature> (versionCode X, flavor dev, SHA Y)
- Phone: PASS/FAIL + screenshot path
- TV: PASS/FAIL + screenshot path + focus notes
- Automated: command + result
- Bugs filed: #n (or none)
```
más el bloque `# Observaciones (DD de Mes del YYYY HH:MM)` → `## Ubicación / Módulo` → `### Categoría`, un bullet por observación con los 3 elementos. Nada de texto libre.
- Si hay hallazgos, no los anuncie ni solicite cambios en medio de la pasada; anótelos y continúe. Solo repórtelos al final y espere confirmación del usuario.
- Si hay observaciones, `REQUEST_CHANGES`, nunca apruebe.
- Nunca apruebe directo. Aunque no haya observaciones, siempre pida autorización al usuario para aprobar.
- Publicar el comentario y el review solo con OK explícito del usuario; nunca auto-publicar al terminar la pasada.
- Si el usuario aprueba al final, entonces acepte la PR, haga squash and merge y borre la rama tras merge OK.

## Out of scope

- Do NOT fix bugs found here. File `issue-bug` and stop.
- Do NOT change build config here. See `gradle-build-run-apk`.
- Do NOT audit parity bookkeeping here. See `parity-phone-tv`.
- Do NOT cambiar de rama. Solo actual salvo especificación explícita.
