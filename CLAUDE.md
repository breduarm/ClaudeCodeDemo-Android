# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Compile debug APK
./gradlew assembleDebug

# Install and run on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.beam.claudecodedemo.ExampleUnitTest"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean assembleDebug
```

## Architecture

The app follows **MVVM + Repository** with a unidirectional data flow:

```
UI (Composable) → ViewModel → Repository → DAO → Room DB
                ↑                        ↓
           StateFlow/collectAsStateWithLifecycle
```

**Dependency Injection:** Hilt (`@HiltAndroidApp` on `TodoApplication`, `@AndroidEntryPoint` on `MainActivity`, `@HiltViewModel` on all ViewModels). Modules live in `di/DatabaseModule.kt` — one `object` for `@Provides` (DB + DAO) and one `abstract class` for `@Binds` (Repository interface).

**Navigation:** Jetpack Navigation Compose with type-safe routes defined in `ui/navigation/NavGraph.kt` as a `sealed class Screen`. The sentinel value `Screen.TaskDetail.NEW_TASK_ID = -1L` distinguishes create vs. edit mode in `TaskDetailViewModel`, which receives `taskId` via `SavedStateHandle`.

**Database:** Room v1 with a single `tasks` table. Schema JSON is exported to `app/schemas/`. No TypeConverters needed — timestamps are stored as `Long` (epoch ms).

**State:** ViewModels expose `StateFlow<UiState>` collected with `collectAsStateWithLifecycle()`. The list ViewModel uses `SharingStarted.WhileSubscribed(5_000)` to survive config changes. The detail ViewModel uses `isSaved: Boolean` in its state to trigger back-navigation via `LaunchedEffect`.

## Package Structure

```
com.beam.claudecodedemo/
├── data/
│   ├── local/          # TaskDao, TaskDatabase (Room)
│   ├── model/          # Task entity
│   └── repository/     # TaskRepository interface + TaskRepositoryImpl
├── di/                 # DatabaseModule (Hilt)
├── ui/
│   ├── navigation/     # NavGraph, Screen sealed class
│   ├── tasklist/       # TaskListScreen + TaskListViewModel
│   ├── taskdetail/     # TaskDetailScreen + TaskDetailViewModel
│   └── theme/          # Material3 theme (Color, Type, Theme)
├── MainActivity.kt
└── TodoApplication.kt
```

## Key Conventions

- All database writes go through `TaskRepository` — never call `TaskDao` directly from a ViewModel.
- `createdAt` is set once on insert and must be preserved on updates. `TaskDetailUiState` carries `createdAt: Long` for this purpose.
- Swipe-to-delete uses `SwipeToDismissBox` (Material3 `ExperimentalMaterial3Api`). Each `LazyColumn` item must have a stable `key = { task.id }`.
- Icons use `androidx.compose.material:material-icons-core` and `material-icons-extended` (both listed in `libs.versions.toml` under `androidx-compose-material-icons-*`).
