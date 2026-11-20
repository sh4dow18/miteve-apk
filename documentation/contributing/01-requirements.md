# Requisitos

> Herramientas, versiones y configuraciones necesarias para trabajar en MiTeVe-APK nativa.

---

## Herramientas obligatorias

| Herramienta | Versión mínima | Propósito |
| ----------- | -------------- | --------- |
| Android Studio | Ladybug o superior | IDE principal (phone + TV) |
| JDK | 17 | Compilar con AGP 8.9.1 (`sourceCompatibility JavaVersion.VERSION_1_8`, toolchain 17 en CI) |
| Android SDK | API 36 (compileSdk 36, build-tools 36.0.0) | Compilación (`compileSdkVersion 36`) |
| Git | Última versión | Control de versiones |

---

## Emuladores obligatorios

Necesitas **ambos** perfiles, porque cada PR exige evidencia phone + TV:

| Perfil | Configuración sugerida | Para qué |
| ------ | ---------------------- | -------- |
| Phone | Pixel 6, API 35, Play Store | `MobileActivity`, touch, PiP, descargas |
| TV | Android TV 1080p, API 35 | `TvActivity` LEANBACK, D-pad, foco, overscan |

Crear ambos desde Device Manager antes del primer build.

---

## Herramientas recomendadas

| Herramienta | Propósito |
| ----------- | --------- |
| **Figma** | Mockups phone + TV (cada feature trae ambas variantes) |
| **Postman** | Probar `BuildConfig.API_HOST` (backend new-api) |
| **OpenCode** | Agente IA para desarrollo |
| GitKraken / GitHub Desktop | Git gráfico |

---

## Permisos en el repositorio

| Rol | Permisos |
| --- | -------- |
| **Programador** | Clonar, push, crear PRs, aprobar PRs, issues, gestión de ramas |
| **Administrador** | Todo lo anterior + settings, branch protection, colaboradores |

---

## Configuración de Git

```bash
git --version
git config user.name "Tu Nombre"
git config user.email "tu.email@ejemplo.com"
```

---

## Verificación de entorno

- [ ] Android Studio instalado con SDK API 36
- [ ] JDK 17 activo (`java -version`)
- [ ] Emulador phone creado
- [ ] Emulador TV (Leanback) creado
- [ ] `local.properties` con `sdk.dir` y datos de keystore (ver [Primeros Pasos](02-getting-started.md))
- [ ] Acceso al repo `sh4dow18/miteve-apk` en GitHub
- [ ] Git configurado con tu identidad
