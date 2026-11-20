---
name: apk-testing
description: Use when adding tests in miteve-apk — JUnit plus Turbine for shared phone/TV ViewModels, Compose UI Test, Maestro flows, connectedDevDebugAndroidTest, QA fixtures, coverage.
license: MIT
compatibility: opencode
---

# APK Testing — unit, UI, Maestro only

This skill covers ONLY testing. It does NOT cover QA manual en emuladores (`apply-qa-apk`), error surfacing (`error-handling-android`), or nav graph (`navigation-pattern`).

## 1. Pirámide (qué va dónde)

- `test/` (JUnit + Turbine): TODOS los `ViewModel` compartidos phone/TV. Un solo test vale para ambos launchers porque el estado es el mismo (`parity-phone-tv`, `compose-structure`).
  ```kotlin
  // app/src/test/kotlin/app/miteve/presentation/feature/player/PlayerViewModelTest.kt
  @Test fun `seek actualiza UiState`() = runTest {
      val vm = PlayerViewModel(FakeRepo())
      vm.onIntent(PlayerIntent.SeekTo(42_000))
      vm.uiState.test {
          assertEquals(42_000, awaitItem().positionMs)
          cancelAndIgnoreRemainingEvents()
      }
  }
  ```
- `androidTest/` (Compose UI Test): una suite phone + una TV por Screen gemela. TV usa D-pad (`performKeyPress(Key.DirectionRight)`), phone usa gestures.
  ```kotlin
  // app/src/androidTest/kotlin/app/miteve/presentation/feature/home/HomeMobileTest.kt
  @get:Rule val compose = createComposeRule()
  @Test fun homeMuestraContinuarViendo() {
      compose.setContent { HomeMobileScreen(state = UiState(QA_SECTIONS), onIntent = {}) }
      compose.onNodeWithText("Continuar viendo").assertIsDisplayed()
  }
  ```
- Maestro (`maestro/`): flows e2e `home → details → player/{id}`, `login → perfil adulto con PIN`. Solo paths críticos, no todo.

## 2. Naming

- Unit: `<Clase>Test.kt` junto al paquete espejo en `src/test/kotlin/app/miteve/...`.
- UI: `<Screen><Mobile|Tv>Test.kt` en `src/androidTest/kotlin/app/miteve/...`.
- Maestro: `maestro/<flow>.yaml` (`maestro/player-flow.yaml`).
- Fixtures: `QA_*` en mayúsculas (`QA_SECTIONS`, `QA_EPISODES`) en `src/androidTest/kotlin/app/miteve/testing/QaFixtures.kt`; nunca datos reales de usuarios ni PII.

## 3. Orden (checklist accionable)

1. Nuevo `UiState`/`Intent` → test Turbine primero (rojo → verde).
2. Nueva Screen gemela → 1 UI test phone + 1 TV (focus inicial asertado en TV).
3. Nuevo flow crítico → Maestro yaml + corre en emulador phone y TV.
4. `./gradlew testDevDebugUnitTest` verde → `connectedDevDebugAndroidTest` en emulador → Maestro `maestro test`.
5. Coverage: `./gradlew app:jacocoTestReport`; mínimo 70% en `presentation/*/ *ViewModel.kt`, sin bajar del baseline.

## 4. Verificación

- [ ] ViewModels compartidos tienen Turbine test (un cambio rompe un solo test, no dos).
- [ ] UI tests phone y TV existen por cada paridad (`parity-phone-tv` tabla al día).
- [ ] `connectedDevDebugAndroidTest` pasa en API 26 (minSdk) + API 34.
- [ ] Fixtures `QA_*`, cero PII, cero llamadas a `new-api` real en tests (fakes/mocks).

## Out of scope

- Do NOT define manual QA adversarial aquí. See `apply-qa-apk`.
- Do NOT define Result/Snackbar aquí. See `error-handling-android`.
- Do NOT renderizar nada en WebView en tests — prohibido salvo OAuth/Custom Tabs.
