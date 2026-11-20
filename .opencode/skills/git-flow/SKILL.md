---
name: git-flow
description: Use when branching, committing, or opening PRs in miteve-apk to enforce conventional branches, atomic commits, and no direct pushes to main.
license: MIT
compatibility: opencode
---

# Git Flow — branches and commits only

This skill covers ONLY branch/commit/PR-open mechanics. It does NOT cover issue content, PR body, or versioning.

## Rules

- Branches from `main`: `feat/<issue>-<slug>`, `fix/<issue>-<slug>`, `chore/<slug>`, `tv/<issue>-<slug>` for TV-only twins. One issue per branch.
- Commits: Conventional Commits (`feat(home): add TvLazyRow focus restore`, `fix(player): correct DASH track fallback`). Atomic, builds green per commit (`./gradlew app:assembleDevDebug`). Never commit `local.properties`, `*.keystore`, `*.jks`, or `google-services.json` if it ever appears.
- Never push to `main` directly. PR required, one approval, `./gradlew build` green. Keep PRs phone+TV together for the same feature (no phone-now-tv-later split — prevents drift, see `parity-phone-tv`).
- Verify: `git status --short` clean except intended files; `git log --oneline -5` shows conventional messages.

## Out of scope

- Do NOT define issue bodies here. See `issue-bug`, `issue-feature`, `issue-task`, `issue-flow`.
- Do NOT define PR body here. See `pr-description`.
- Do NOT define tags here. See `version-tag`.
