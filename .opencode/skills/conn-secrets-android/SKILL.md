---
name: conn-secrets-android
description: Use when adding or referencing any secret, keystore credential, API host, or token in miteve-apk (local.properties + BuildConfig only, never git).
license: MIT
compatibility: opencode
---

# Conn Secrets Android — secrets handling only

This skill covers ONLY where secrets live and how code reads them. It does NOT cover auth logic (`api-client-auth`) or signing task shape (`gradle-build-run-apk`).

## Rules

- All secrets in git-ignored `local.properties` at repo root (never committed): `store.file`, `store.password`, `key.alias`, `key.password` (release signing — fixes the broken `/home/sh4dow18/...` TWA path), `api.host` (local override, e.g. `http://10.0.2.2:8080`). Verify ignored: `git check-ignore local.properties` must print it.
- Code reads via `BuildConfig` fields generated per flavor (`API_HOST`), and DataStore at runtime for the JWT (`miteve_token`). No `BuildConfig` secret beyond host/keys that are safe in the APK; no secret string literals in `.kt` — verify with `rg -n "password|secret|api_key" app/src/main/kotlin -i` (only `BuildConfig`/DataStore accessors may hit).
- JWT lives ONLY in DataStore (see `api-client-auth`). Keystore files (`*.keystore`, `*.jks`) never enter the repo; CI injects them from runner secrets into `local.properties` at build time.
- If a secret leaks into git history: rotate it, then notify — this skill does not cover history rewriting.

## Out of scope

- Do NOT define auth interceptor logic here. See `api-client-auth`.
- Do NOT define flavor structure here. See `gradle-build-run-apk`.
- Do NOT paste secret values into issues/PRs. See `issue-flow`, `pr-description`.
