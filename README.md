# Transaction
## Project Overview

This is a test project developed to demonstrate the implementation of modern Android development practices. The application communicates with a REST API that provides user authentication through login functionality and token-based authorization.

For secure storage of the authentication token, I used EncryptedSharedPreferences, ensuring that sensitive data remains protected even if the device is compromised. All network operations are handled using Retrofit, a type-safe HTTP client for Android and Java, which simplifies API communication and parsing of JSON responses.

To manage dependencies efficiently and promote clean architecture, I integrated Dagger Hilt for dependency injection. This helps in creating a scalable and maintainable codebase by providing a structured way to inject dependencies across various layers of the application.

Overall, the project showcases best practices in Android development, focusing on security, scalability, and clean code principles.
