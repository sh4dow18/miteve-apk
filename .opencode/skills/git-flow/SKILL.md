---
name: git-flow
description: Use when branching, committing, or opening PRs in miteve-apk to enforce conventional branches, atomic commits, and no direct pushes to main.
license: MIT
compatibility: opencode
---

# Git Flow — branches and commits only (develop-based, NOT GitHub Flow)

This skill covers ONLY branch/commit/PR-open mechanics. It does NOT cover issue content, PR body, or versioning.

## Rules

- Base `develop`: all work branches start from `develop` and merge back to it. Protected: `develop` PR + 1 approval, `main` PR from `develop` + 2 approvals, `release/*` hotfix only + 2 approvals. Exempt from lint: `main`, `develop`, `release/*`, `hotfix/*`.
- Naming: `Tipo/<issue>-descripcion-corta`, types `Feature|Fix|Refactor|Docs` (capitalized). Regex CI: `^(Feature|Fix|Refactor|Docs)/[0-9]+-[a-z0-9-]+$`. One issue per branch. `Docs/*` only accepts `docs:` commits.
- Commits: `#<issue> <tipo>: <desc>` in English lowercase, types `feat|fix|refactor|docs|style|test|chore`. The `#` must match the branch number. Atomic, `./gradlew app:assembleDevDebug` green per commit. Never commit `local.properties`, `*.keystore`, `*.jks`, or `google-services.json` if it ever appears.
- PR: title = exact branch name (except checkpoints `develop → main`, `main → release/*`); base `develop`; full template + `Phone vs TV`. Reviewer auto: `sh4dow18`. Never push to `main`/`develop` directly. Keep phone+TV together (no phone-now-tv-later split — see `parity-phone-tv`).
- Verify: `git status --short` clean except intended files; `git log --oneline -5` shows `#<issue> <tipo>:` messages; CI `branch-lint.yml` + `commitlint.yml` green.

## Out of scope

- Do NOT define issue bodies here. See `issue-bug`, `issue-feature`, `issue-task`, `issue-flow`.
- Do NOT define PR body here. See `pr-description`.
- Do NOT define tags here. See `version-tag`.
