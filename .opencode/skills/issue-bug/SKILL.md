---
name: issue-bug
description: Use when filing a bug report for miteve-apk (native phone/TV, Media3, Room sync, dual launcher).
license: MIT
compatibility: opencode
---

# Issue Bug — bug report shape only

This skill covers ONLY bug issue bodies. Process mechanics live in `issue-flow`.

## Template (all sections mandatory, no placeholders left blank)

```markdown
## Actual (phone / TV — state which)
<!-- What happens, on which form factor + flavor (dev/prod), versionCode -->

## Expected
<!-- Parity reference: web behavior or api contract -->

## Steps (numbered, reproducible on emulator)
1.
2.

## Evidence
<!-- logcat snippet + mobile screenshot AND TV screenshot where UI is involved -->

## Suspected layer
<!-- presentation / domain / data / build — one only, per android-clean-architecture -->
```

- Form-factor field is mandatory: every bug states phone, TV, or both (run `parity-phone-tv` audit when both differ).
- No `TBD`/`TODO` placeholders. If unknown, write `unknown — needs triage` explicitly.

## Out of scope

- Do NOT define workflow/labels here. See `issue-flow`.
- Do NOT define QA execution here. See `apply-qa-apk`.
