---
name: biometric-auth
description: Use when gating adult profiles with BiometricPrompt in miteve-apk — adapted from dh-vimax media-biometria, parental PIN fallback, never store PIN in cleartext.
license: MIT
compatibility: opencode
---

# Biometric Auth — perfiles adultos only

This skill covers ONLY biometría + PIN parental. No cubre login JWT (`api-client-auth`) ni DataStore genérico (`room-sync`).

## 1. Checklist accionable (adaptado de `media-biometria` dh-vimax)

- `BiometricPrompt` al abrir perfil adulto; perfiles infantiles NUNCA lo piden. Fallback PIN parental de 4 dígitos:
  ```kotlin
  // app/src/main/kotlin/app/miteve/presentation/feature/profiles/AdultGate.kt
  fun authenticateAdult(activity: FragmentActivity, onOk: () -> Unit, onPinFallback: () -> Unit) {
      BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
          override fun onAuthenticationSucceeded(r: BiometricPrompt.AuthenticationResult) = onOk()
          override fun onAuthenticationError(code: Int, msg: CharSequence) { if (code != BiometricPrompt.BIOMETRIC_SUCCESS) onPinFallback() }
      }).authenticate(BiometricPrompt.PromptInfo.Builder()
          .setTitle("Perfil adulto").setSubtitle("Confirma tu huella o rostro").setNegativeButtonText("Usar PIN").build(),
          BiometricPrompt.CryptoObject(/* cipher si se requiere unlock fuerte */))
  }
  ```

## 2. Nunca PIN en claro (OBLIGATORIO)

- PIN = hash SHA-256 + salt aleatorio en `EncryptedSharedPreferences` (o DataStore cifrado). Comparar hashes, jamás el PIN:
  ```kotlin
  // app/src/main/kotlin/app/miteve/data/local/PinStore.kt
  class PinStore @Inject constructor(@ApplicationContext ctx: Context) {
      private val prefs = EncryptedSharedPreferences.create(ctx, "parental", MasterKey.Builder(ctx).build(),
          EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
      fun setPin(pin: String) { val salt = UUID.randomUUID().toString(); prefs.edit().putString("salt", salt).putString("hash", sha256(salt + pin)).apply() }
      fun check(pin: String): Boolean = sha256(prefs.getString("salt","") + pin) == prefs.getString("hash","")
  }
  ```

## 3. Naming

- `presentation/feature/profiles/AdultGate.kt`, `PinPad{Mobile,Tv}Screen.kt` (TV: D-pad numérico con foco), `data/local/PinStore.kt`.

## 4. Verificación

- [ ] `grep -r "pin" --include=*.kt` no muestra literales ni `putString("pin", <dígitos>)`.
- [ ] 5 intentos PIN fallidos → bloqueo 60s + Snackbar ES.
- [ ] TV sin biometría → PIN directo sin error; phone sin biometría → PIN directo.
- [ ] Test (`apk-testing`): `PinStore` hash round-trip + bloqueo tras intentos.

## Out of scope

- Do NOT definir login/sesión aquí. See `api-client-auth`.
- Do NOT definir entidades Room aquí. See `room-sync`.
- Do NOT autenticar dentro de WebView — prohibido salvo OAuth/Custom Tabs.
