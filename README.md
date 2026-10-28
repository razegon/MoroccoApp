# MoroccoApp

**MoroccoApp** is a travel guide application focused on discovering Morocco. It provides information about places, cities, cultural attractions, and points of interest, with additional features for quizzes and arcade-style games.

The project is built with Kotlin and Jetpack Compose and is being migrated to Kotlin Multiplatform (KMP) and Compose Multiplatform to share more code across platforms.

## Features

- Explore cities, places, and cultural attractions in Morocco.
- View detailed information about places.
- Retrieve additional information from Wikidata.
- Discover images from Wikimedia Commons with attribution.
- Play quizzes about Morocco.
- Play arcade-style games and view rankings.
- Cache selected data locally for improved performance and persistence.

## Tech Stack

- **Kotlin**
- **Kotlin Multiplatform (KMP)**
- **Jetpack Compose / Compose Multiplatform**
- **Android Gradle Plugin and Gradle**
- **Room** for local database storage
- **Retrofit** for API communication
- **Wikidata API** for structured information
- **Wikimedia Commons** for images

## Project Structure

```text
MoroccoApp/
├── app/       # Android application, platform integration and app variants
├── shared/    # Shared KMP code and Compose Multiplatform UI
├── gradle/    # Gradle wrapper and build configuration
└── ...
```

The `shared` module contains shared domain models, repositories, data sources, caching, presentation logic, and the UI being migrated to Compose Multiplatform.

The `app` module contains Android-specific integration and the Android application entry point. The migration is ongoing, so some screens and components still reside in `app`.

## Android Build

Open the project in Android Studio with a compatible JDK and Android SDK installed.

To build both Android debug variants on Windows:

```bash
gradlew.bat :app:assembleStableDebug :app:assembleDevDebug
```

On macOS or Linux:

```bash
./gradlew :app:assembleStableDebug :app:assembleDevDebug
```

The project provides two Android variants:

- **Stable** — the regular application variant.
- **Dev** — a development variant that can be installed separately from Stable.

## Development Workflow

The project uses the following Git workflow:

```text
feature/* → dev → main
```

Feature branches are merged into `dev` for integration and testing. Changes are promoted to `main` after verification.

## Project Status

MoroccoApp is actively being developed. The Kotlin Multiplatform migration is in progress, with shared business logic already in place and UI migration continuing.

## Data Sources

- [Wikidata](https://www.wikidata.org/)
- [Wikimedia Commons](https://commons.wikimedia.org/)

Images and external data remain subject to the applicable source licenses and attribution requirements.
