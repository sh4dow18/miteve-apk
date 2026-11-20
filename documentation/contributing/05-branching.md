# Estrategia de Ramas

> Naming convention y flujo de branching en MiTeVe-APK (igual que dh-vimax).

---

## Rama base

La rama principal de desarrollo es **`develop`**. Todas las ramas nacen desde `develop` y se fusionan de vuelta a ella.

```text
develop ─────●─────●─────●─────●─────●─────●─────●──────
               \         /       \         /
                ●───────●         ●───────●
             Feature/123        Fix/456
```

---

## Naming convention

### Formato general

```text
Tipo/<issue>-descripción-corta
```

### Tipos de ramas

| Tipo | Propósito | Ejemplo |
| ---- | --------- | ------- |
| `Feature/` | Nuevas funcionalidades | `Feature/123-player-offline-downloads` |
| `Fix/` | Corrección de bugs | `Fix/124-tv-focus-player-controls` |
| `Refactor/` | Refactorización | `Refactor/125-migrate-nav-graph-tv` |
| `Docs/` | Solo documentación | `Docs/126-update-contributing-apk` |

> **Nota Docs:** en ramas `Docs/123-...` solo se permiten commits `#123 docs: ...`.

### Reglas de naming

1. **Siempre incluir el número de issue**.
2. Descripción corta: máximo 4-5 palabras.
3. Separador: guión `-` entre palabras.
4. Sin caracteres especiales: solo letras, números y guiones.
5. Minúsculas en la descripción.

### Ejemplos reales del proyecto

```text
Feature/123-player-offline-downloads
Fix/124-tv-focus-player-controls
Refactor/125-migrate-nav-graph-tv
Docs/126-update-contributing-apk
Fix/127-auth-refresh-token-expiry
Feature/128-downloads-manager-wifi-only
```

---

## Crear una rama

```bash
git checkout develop
git pull origin develop
git checkout -b Feature/123-player-offline-downloads
git branch  # debe mostrar * Feature/123-player-offline-downloads
```

---

## Sincronizar con develop

```bash
git checkout develop
git pull origin develop
git checkout Feature/123-player-offline-downloads
git merge develop
```

O con rebase:

```bash
git checkout Feature/123-player-offline-downloads
git rebase develop
```

---

## Después del merge

```bash
git checkout develop
git pull origin develop
git branch -d Feature/123-player-offline-downloads
git push origin --delete Feature/123-player-offline-downloads
```

---

## Validación automática

Misma regex que dh-vimax, `^(Feature|Fix|Refactor|Docs)/[0-9]+-[a-z0-9-]+$`:

| Dónde | Archivo | Cuándo valida |
| ----- | ------- | ------------- |
| Remoto (GitHub) | `.github/workflows/branch-lint.yml` | En cada `create`/`push`/`pull_request` |
| Remoto (GitHub) | `.github/workflows/commitlint.yml` | Al abrir/actualizar PR (además valida correlación rama↔commit) |

```bash
^(Feature|Fix|Refactor|Docs)/[0-9]+-[a-z0-9-]+$
# Válido:   Feature/123-player-offline
# Inválido: feature/123-player (tipo minúscula), Feature/player (falta 123), Feature/#123-player (con #)
```

Exentas: `main`, `develop`, `release/*`, `hotfix/*`.

Renombrar sin perder commits: `git branch -m Vieja Nueva && git push -u origin Nueva && git push origin --delete Vieja` (si ya hay PR, abrir uno nuevo).

---

## Ramas protegidas

| Rama | Protección |
| ---- | ---------- |
| `develop` | PR + al menos 1 aprobación |
| `main` | PR desde `develop` + 2 aprobaciones |
| `release/*` | Solo por hotfix + 2 aprobaciones |

---

## Errores comunes

| Error | Solución |
| ----- | -------- |
| Crear rama desde main | `git checkout -b Feature/123-desc develop` |
| Olvidar el número de issue | Renombrar: `git branch -m Feature/desc Feature/123-desc` |
| Conflicto con develop | Merge o rebase de develop primero |
| Push sin upstream | `git push -u origin Feature/123-desc` |
