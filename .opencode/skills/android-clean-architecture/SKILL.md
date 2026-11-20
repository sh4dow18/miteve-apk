---
name: android-clean-architecture
description: Use when creating or moving any Kotlin source in miteve-apk to enforce presentation-domain-data layers, feature packages, and one-way dependencies.
license: MIT
compatibility: opencode
---

# Android Clean Architecture — layer separation only

This skill covers ONLY package/layer placement for the native app. It does NOT cover formatting, theming, auth detail, or secrets.

## Layers (base `app/src/main/kotlin/app/miteve/`)

- `presentation/` (Compose only): `feature/home/HomeScreen.kt`, `feature/home/HomeViewModel.kt`, `feature/home/HomeUiState.kt`, `components/ContentCard.kt`. Renders state, emits intents. No Retrofit, no Room, no `OkHttpClient` imports.
- `domain/` (pure Kotlin, no Android imports except `androidx` annotations-free): `model/Content.kt`, `usecase/GetContinueWatchingUseCase.kt`, `repository/ContentRepository.kt` (interface). No DTOs, no `@Entity`, no Retrofit annotations.
- `data/` (implementations): `repository/ContentRepositoryImpl.kt`, `datasource/remote/ContentRemoteDataSource.kt`, `datasource/local/ContentLocalDataSource.kt`, `api/dto/ContentDto.kt`, `api/MiteveApi.kt`, `db/entity/ContentEntity.kt`, `db/dao/ContentDao.kt`, `mapper/ContentMapper.kt` (DTO↔domain, Entity↔domain).
- `di/` Hilt modules only: `NetworkModule.kt`, `DatabaseModule.kt`, `RepositoryModule.kt`. `core/` for cross-cutting: `core/result/ApiResult.kt`, `core/error/ErrorHandler.kt`.

## 1:1 web mapping (miteve-web → miteve-apk)

| web (Next.js FSD) | native target |
|---|---|
| `features/*` (web business unit) | `presentation/feature/*` (Screen + ViewModel + UiState) |
| `entities/content` | `domain/content` (model + repository interface + use cases) |
| `widgets/*` | `presentation/components/*` (shared composables) |
| `shared/ui/*` | `core/ui/*` + Material3 theme (see `compose-theming`) |

- Barrels prohibited: no `index.ts`-style re-export files, no `package object` aggregators. Import directly: `import app.miteve.domain.model.Content`.

## Direction (single, enforced)

`presentation -> domain <- data` (domain knows nobody; data implements domain interfaces; presentation depends only on domain + Hilt).

```kotlin
// app/src/main/kotlin/app/miteve/domain/usecase/GetContentUseCase.kt
class GetContentUseCase @Inject constructor(
    private val repository: ContentRepository, // interface, not impl
) {
    suspend operator fun invoke(id: String): ApiResult<Content> = repository.getContent(id)
}
```

## Violation examples to fix

- `Retrofit`/`@Dao` import inside `HomeScreen.kt` → move call to `domain/usecase`, expose via `HomeViewModel.uiState`.
- Price/business `if` inside a composable row → move to `domain/model` pure function or use case, composable only renders.
- New `Button` copy-paste in 3 screens → reuse `presentation/components/MiteveButton.kt`.
- 300-line Screen → split to `components/*` + `HomeContent` stateless overload, route stays < 60 lines composing.

## Out of scope

- Do NOT define composable content rules here. See `compose-structure`.
- Do NOT define API/auth detail here. See `api-client-auth`.
- Do NOT define Room sync detail here. See `room-sync`.
