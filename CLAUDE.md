# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Kotlin Multiplatform (KMP) markdown viewer with Mermaid diagram support, targeting Android and iOS using Compose Multiplatform. See `SPECIFICATION.md` for detailed requirements.

## Build Commands

```bash
# Android debug build
./gradlew :composeApp:assembleDebug

# Android release build
./gradlew :composeApp:assembleRelease

# Run all tests
./gradlew :composeApp:allTests

# Run common tests only
./gradlew :composeApp:testDebugUnitTest

# Clean build
./gradlew clean

# Check dependencies
./gradlew dependencies
```

**iOS:** Open `iosApp/` directory in Xcode and build from there.

## Architecture

### Source Set Structure

```
composeApp/src/
├── commonMain/kotlin/com/markduenas/markdownviewer/
│   ├── App.kt                    # Root composable entry point
│   ├── data/
│   │   ├── HttpClientFactory.kt  # expect declaration for HTTP client
│   │   └── UrlFetcher.kt         # URL content fetching with Ktor
│   ├── model/
│   │   ├── ContentSource.kt      # LocalFile/RemoteUrl sealed class
│   │   ├── ViewerState.kt        # Main UI state
│   │   └── RecentItem.kt         # Recent files model
│   └── ui/
│       ├── components/           # EmptyState, MarkdownContent, LoadingIndicator, ErrorDisplay
│       ├── dialogs/              # UrlInputDialog
│       └── theme/                # Theme.kt, Typography.kt (orange Material 3)
├── androidMain/kotlin/.../
│   ├── data/HttpClientFactory.android.kt  # Android Ktor engine
│   └── model/RecentItem.android.kt        # currentTimeMillis actual
└── iosMain/kotlin/.../
    ├── data/HttpClientFactory.ios.kt      # Darwin Ktor engine
    └── model/RecentItem.ios.kt            # currentTimeMillis actual
```

### expect/actual Pattern

Platform-specific code uses Kotlin's `expect`/`actual` mechanism:
- `HttpClientFactory.kt` - HTTP client creation (Android uses Android engine, iOS uses Darwin)
- `RecentItem.kt` - `currentTimeMillis()` function

### Key Dependencies

- `com.mikepenz:multiplatform-markdown-renderer` - Markdown rendering
- `io.ktor:ktor-client-*` - HTTP client for URL fetching
- `io.github.vinceglb:filekit-dialogs-compose` - Cross-platform file picker
- `io.coil-kt.coil3:coil-compose` - Image loading in markdown

### Platform Integration

- **Android:** Intent filters in `AndroidManifest.xml` for opening .md files
- **iOS:** `CFBundleDocumentTypes` in `Info.plist` for document type registration

## Package Structure

All code under `com.markduenas.markdownviewer`:
- `data/` - Data layer (HTTP clients, fetchers)
- `model/` - State and domain models
- `ui/components/` - Reusable UI components
- `ui/dialogs/` - Dialog composables
- `ui/theme/` - Material 3 theme configuration

## Design

- **Primary color: Orange** (#FF6D00 seed) - Material 3 theme
- Light/dark theme follows system preference
- Max content width ~800dp for readability

## Remaining Work

- Mermaid diagram rendering via WebView (Phase 4)
- Recent files persistence (Phase 6)
