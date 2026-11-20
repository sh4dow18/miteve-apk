# Convenciones de Commits

> Formato, tipos y buenas prácticas (igual que dh-vimax).

---

## Formato del mensaje

```text
#<issue> <tipo>: <descripción>
```

### Ejemplos (reales del dominio APK)

```text
#123 feat: add offline downloads with media3
#124 fix: resolve tv focus loss in player controls
#125 refactor: share player uistate between mobile and tv
#126 docs: update contributing guide for apk
```

---

## Tipos de commit

| Tipo | Propósito | Ejemplo |
| ---- | --------- | ------- |
| `feat` | Nueva funcionalidad | `#123 feat: add offline downloads with media3` |
| `fix` | Corrección de bug | `#124 fix: resolve tv focus loss in player controls` |
| `refactor` | Refactorización sin cambio de comportamiento | `#125 refactor: share player uistate between mobile and tv` |
| `docs` | Cambios en documentación | `#126 docs: update contributing guide for apk` |
| `style` | Formato (ktfmt, sin cambio de código) | `#127 style: apply ktfmt to downloads feature` |
| `test` | Agregar o modificar tests | `#128 test: add turbine test for downloads viewmodel` |
| `chore` | Mantenimiento | `#129 chore: bump agp to 8.9.1` |

---

## Reglas

1. **Siempre incluir el número de issue al inicio** (`#123 ...`, nunca sin `#`).
2. **Un commit = un cambio lógico** (no mezclar feature + fix en uno).
3. **Mensaje descriptivo** (`#124 fix: resolve tv focus loss...`, no `#124 fix: change code`).
4. **En inglés**, minúsculas después del tipo, sin punto final.

---

## Validación automática

`commitlint.yml` valida en cada PR:

- Formato `^#[0-9]+ (feat|fix|refactor|docs|style|test|chore): .+` (además permite `Fix/123-... (#PR)` de Squash y `Merge ...`).
- Que el `#` del commit coincida con el `#` de la rama (en `Fix/123-...` solo `#123 ...`).
- En `Docs/*` solo `docs: ...`.
- Título del PR = nombre exacto de la rama (excepto checkpoints `develop → main`, `main → release/*`).

---

## Qué NO commitear

| Archivo / Carpeta | Razón |
| ----------------- | ----- |
| `local.properties` | SDK path + passwords del keystore |
| `*.jks`, `*.keystore` | Claves de firma Play Store |
| `google-services.json` local con datos reales | Credenciales |
| `build/` | Artefactos de Gradle |
| `.idea/` personales, `*.iml` locales | Config local del IDE |
| `keystore.properties` con passwords reales | Secretos |

---

## Verificar antes de commitear

- [ ] `./gradlew app:assembleDevDebug` en verde
- [ ] Probado en emulador phone **y** TV (si toca UI)
- [ ] El mensaje sigue `#<issue> <tipo>: <desc>` en inglés
- [ ] No incluye secretos (`local.properties`, keystore)
- [ ] El commit es atómico (un solo cambio lógico)
