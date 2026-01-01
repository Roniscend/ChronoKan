CONTRIBUTION WORKFLOW



1\. Fork the repository on GitHub

2\. Clone your fork locally

3\. Create a new branch from main

4\. Make your changes following the project architecture and conventions

5\. Commit with clear and descriptive messages

6\. Open a pull request targeting the main branch



--------------------------------------------------



GETTING THE PROJECT RUNNING



CLONE THE REPOSITORY



git clone https://github.com/<your-username>/ChronoKan.git

cd ChronoKan



--------------------------------------------------



ANDROID SETUP



\- Android Studio Ladybug or newer is recommended

\- Android SDK with API level 26 or higher

\- Allow Gradle to complete syncing after opening the project



--------------------------------------------------



FIREBASE CONFIGURATION



ChronoKan uses Firebase Authentication.



Steps:

1\. Create a Firebase project in Firebase Console

2\. Enable Email/Password Authentication

3\. Enable Google Sign-In (optional)

4\. Download google-services.json

5\. Place it inside the app/ directory

6\. Rebuild the project



--------------------------------------------------



CODE GUIDELINES



\- Follow MVVM (Model–View–ViewModel) architecture

\- Use Jetpack Compose best practices

\- Keep composables small and reusable

\- Avoid placing business logic inside composables

\- Prefer immutable state and unidirectional data flow

\- Use Kotlin coroutines for asynchronous operations

\- Avoid introducing unnecessary dependencies



--------------------------------------------------



PROJECT STRUCTURE EXPECTATIONS



\- UI code belongs in the ui package

\- Business logic must reside in ViewModels

\- Data access should go through repositories

\- Room entities and DAOs should remain in the data layer



--------------------------------------------------





