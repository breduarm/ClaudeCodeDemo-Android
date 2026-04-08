# ClaudeCodeDemo — Android Todo App

A task manager Android app built as a demo project for [Claude Code](https://claude.ai/code). It showcases modern Android development with Jetpack Compose and a clean MVVM + Repository architecture.

## Features

- Create, edit, and delete tasks
- Mark tasks as completed
- Swipe to delete
- Persisted locally with Room (survives app restarts)
- Creation and last-updated timestamps per task

---

## Tech Stack

| Technology | Version |
|---|---|
| Kotlin | 2.3.20 |
| Jetpack Compose BOM | 2026.03.01 |
| Material 3 | via BOM |
| Architecture | MVVM + Repository |
| Dependency Injection | Hilt 2.59.2 |
| Navigation | Navigation Compose 2.9.7 |
| Database | Room 2.8.4 |
| Async | Kotlin Coroutines + StateFlow |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

---

## Architecture

Unidirectional data flow:

```
UI (Composable) → ViewModel → Repository → DAO → Room DB
                ↑                        ↓
           StateFlow/collectAsStateWithLifecycle
```

- **UI** — Jetpack Compose screens that collect `StateFlow<UiState>` via `collectAsStateWithLifecycle()`.
- **ViewModel** — holds UI state, handles user events, delegates persistence to the repository. Injected by Hilt (`@HiltViewModel`).
- **Repository** — single source of truth. `TaskRepository` interface with `TaskRepositoryImpl`. Injected via `@Binds` in `DatabaseModule`.
- **DAO** — Room `TaskDao` exposes suspend functions and `Flow` queries.
- **Database** — single `tasks` table. Timestamps stored as `Long` (epoch ms). Schema exported to `app/schemas/`.

---

## Package Structure

```
com.beam.claudecodedemo/
├── data/
│   ├── local/          # TaskDao, TaskDatabase (Room)
│   ├── model/          # Task entity
│   └── repository/     # TaskRepository interface + TaskRepositoryImpl
├── di/                 # DatabaseModule (Hilt)
├── ui/
│   ├── navigation/     # NavGraph, Screen sealed class (type-safe routes)
│   ├── tasklist/       # TaskListScreen + TaskListViewModel
│   ├── taskdetail/     # TaskDetailScreen + TaskDetailViewModel
│   └── theme/          # Material3 theme (Color, Type, Theme)
├── MainActivity.kt
└── TodoApplication.kt
```

---

## Prerequisites

- Android Studio Meerkat (2024.3) or later
- JDK 11+
- Android SDK 36 installed
- A connected device or emulator running API 24+

---

## Getting Started

```bash
# 1. Clone the repository
git clone https://github.com/breduarm/ClaudeCodeDemo-Android.git
cd ClaudeCodeDemo-Android

# 2. Open in Android Studio
# File → Open → select the cloned folder

# 3. Build and install on a connected device/emulator
./gradlew installDebug
```

---

## Build Commands

```bash
# Compile debug APK
./gradlew assembleDebug

# Install and run on connected device/emulator
./gradlew installDebug

# Clean build
./gradlew clean assembleDebug
```

---

## Running Tests

```bash
# Unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.beam.claudecodedemo.ExampleUnitTest"

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

---

## License

This project is open source and available under the [MIT License](LICENSE).