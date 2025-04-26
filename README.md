# iRecipe Android App 🍳

A modern Android application for discovering and exploring recipes, built with best practices in
mind. This project demonstrates Clean Architecture, modern Android development tools, and secure
data handling.

## Features ✨

- **Recipe Browsing**: View a curated list of recipes with beautiful images
- **Search Functionality**: Find recipes by name, ingredients, or type (3+ character threshold)
- **Offline Support**: Recipes are cached locally using encrypted Room database
- **Recipe Details**: Detailed view with ingredients, instructions, and base ingredients
- **Error Handling**: Graceful error states and recovery options
- **Secure Storage**: Encrypted local database using SQLCipher
- **Dark/Light Theme**: Full Material You theming support

## Architecture 🏛️

**Clean Architecture** implementation with clear separation of concerns:

📁 app

├── 📁 data # Data layer (API, Database, Mappers, Security)

├── 📁 domain # Business logic (Models, Use Cases, Repository interfaces)

├── 📁 presentation # UI layer (Composables, ViewModels, Activities, Navigation)

└── 📁 di # Dependency Injection (Koin modules)

Key principles:

- **SOLID** compliant design
- Unidirectional data flow
- Reactive UI with Jetpack Compose
- Multi-module ready structure

## Tech Stack 🛠️

- **Language**: 100% Kotlin
- **UI**: Jetpack Compose + Material 3
- **DI**: Koin
- **Network**: Retrofit + OkHttp
- **Local DB**: Room + SQLCipher encryption
- **Async**: Kotlin Coroutines + Flow
- **Navigation**: Compose Navigation
- **Security**: Android Keystore + EncryptedSharedPreferences
- **Image Loading**: Coil
- **Testing**: JUnit, MockK, Robolectric

## Security 🔒

- AES-256 database encryption
- Secure key management via Android Keystore
- Encrypted shared preferences

## Testing 🧪

Comprehensive test coverage including:

- ViewModel unit tests
- Repository integration tests
- Use case validation
- Data source verification
- Error handling scenarios
- Coroutine testing

## API Consumption

The recipes API used is located at the following GitHub repository:

- https://github.com/DenilsonRabelo/API-Receitas

Feel free to use and/or build your own.

## TODO List

- [ ] Add a splash screen
- [ ] Add multiline comments within recipes (with Camera/image feature, with WorkManager and Push
  Notification after upload is done)
- [ ] Pull-to-refresh on Recipes list
- [ ] Favorite recipe feature (add, remove, new screen)
- [ ] Multi-module architecture (separate domain and data layer from presentation to create a
  library for external usage)
- [ ] Add support for Proguard
- [ ] UI and Components testing with Espresso
- [ ] Abstraction of Logging services
- [ ] Implementation of API Tokens
- [ ] Implementation of Pagination in Recipes List (requires back-end support)
- [ ] Implementation of Performance, App Distribution and Analytics features from Firebase

## Setup 🚀

1. Clone the repository
2. Open in Android Studio
3. Configure API endpoint in `RetrofitClient.kt` file:
   BASE_URL="https://api-receitas-pi.vercel.app/"
4. Build and run!

Note: This project is for educational purposes and demonstrates modern Android development patterns.
Replace API endpoints and security configurations for production use.

