# University Portal Android App

A modern Android application built with clean architecture principles, dependency injection using Hilt, and comprehensive testing.

## Architecture

This project follows **Clean Architecture** principles with the following layers:

### Domain Layer
- **Use Cases**: Business logic encapsulation
  - `LoginUseCase`: Handles user authentication
  - `GetDashboardUseCase`: Manages dashboard data retrieval

### Data Layer
- **Repository**: Single source of truth for data
- **Network**: API service and data models
- **DataStore**: Local data persistence for user preferences

### Presentation Layer
- **ViewModels**: UI state management with MVVM pattern
- **Activities**: UI components
- **Adapters**: RecyclerView data binding

## Testing Strategy

### Unit Tests
- **Use Case Tests**: Business logic validation
- **ViewModel Tests**: UI state management testing
- **Repository Tests**: Data layer testing

### Integration Tests
- **Hilt Tests**: Dependency injection verification
- **UI Tests**: End-to-end user interaction testing

### Test Coverage
- ✅ Login functionality
- ✅ Dashboard data loading
- ✅ Error handling
- ✅ State management
- ✅ Dependency injection

## Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **UI**: Material Design Components
- **Testing**: JUnit, MockK, Espresso
- **Build System**: Gradle

## Project Structure

```
app/src/main/java/com/example/s8131464assignment/
├── data/                    # Data layer
│   ├── Repository.kt       # Data repository
│   └── Result.kt          # Result wrapper
├── datastore/             # Local storage
│   └── KeypassStore.kt    # User preferences
├── di/                    # Dependency injection
│   └── AppModule.kt       # Hilt modules
├── domain/                # Domain layer
│   └── usecase/           # Business logic
│       ├── LoginUseCase.kt
│       └── GetDashboardUseCase.kt
├── network/               # Network layer
│   ├── ApiService.kt      # API interface
│   ├── RetrofitClient.kt  # Network client
│   └── models/            # Data models
├── ui/                    # Presentation layer
│   ├── LoginActivity.kt
│   ├── LoginViewModel.kt
│   ├── DashboardActivity.kt
│   ├── DashboardViewModel.kt
│   └── adapters/
└── MyApp.kt              # Application class
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17
- Android SDK 24+

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run the app

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## UI Features

- **Modern Design**: Material Design 3 components
- **University Branding**: Professional color scheme
- **Responsive Layout**: Adaptive to different screen sizes
- **Accessibility**: Screen reader support
- **Dark Mode**: System theme integration

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused

### Testing
- Write tests for all business logic
- Maintain >80% test coverage
- Use descriptive test names
- Mock external dependencies

### Git Workflow
- Use conventional commit messages
- Create feature branches
- Review code before merging
- Keep commits atomic

## Features

- **Secure Login**: Campus-based authentication
- **Academic Dashboard**: Course and student information
- **Offline Support**: Cached data for better UX
- **Error Handling**: User-friendly error messages
- **Loading States**: Visual feedback during operations

## Known Issues

- Emulator storage space requirements
- Network timeout handling
- Large dataset performance

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Team

- **Developer**: Bidhan Bhatta
- **Course**: Mobile Development
- **Institution**: University Portal System

---

**Note**: This is an academic project demonstrating modern Android development practices with clean architecture, dependency injection, and comprehensive testing.
