🚀 ChronoKan: The Time-Traveling Kanban Board
ChronoKan is a modern productivity application that combines the classic efficiency of a Kanban board with "Time Travel" capabilities, allowing users to scrub through their task history and visualize progress over time.

✨ Key Features
Secure Authentication: Integrated with Firebase Auth for Email/Password and Google One-Tap Sign-in.


Kanban Workflow: Organizes tasks into Todo, Progress, and Done states.


Time-Travel Scrubber: A persistent history scrubber at the bottom of the screen allows users to "rewind" the board to see previous states of their project.

Superhero Identities: Every user is assigned a unique, consistent Superhero Name generated from their unique Firebase UID.

Reactive UI: Built entirely with Jetpack Compose for a smooth, modern Android experience.

Local Persistence: Uses Room Database to ensure tasks are saved locally even when offline.

🛠️ Tech Stack
Language: Kotlin

UI Framework: Jetpack Compose (Material 3)

Backend: Firebase (Authentication)

Database: Room (Local SQL storage)

Navigation: Jetpack Compose Navigation

Image Loading: Coil (for Google Profile pictures)

Architecture: MVVM (Model-View-ViewModel)

📸 Screenshots
Login & Security	Kanban Board	Hero Profile

Export to Sheets

🚀 Getting Started
Prerequisites
Android Studio Ladybug or newer.

A Firebase project set up in the Firebase Console.

Installation
Clone the repo:


Bash
git clone https://github.com/Roniscend/ChronoKan.git
Add Firebase:

Place your google-services.json in the app/ directory.

Configure Google Sign-In:
Add your default_web_client_id to res/values/strings.xml.

Sync & Run:

Sync Gradle and run the app on an emulator or physical device.

🏗️ Project Structure
Plaintext

com.example.chronokan
├── data                # Room Database, DAOs, and Repository
├── ui
│   ├── auth           # LoginScreen, ProfileScreen, AuthViewModel
│   ├── board          # TaskListScreen, BoardViewModel
│   ├── navigation     # ChronosNavGraph
│   └── theme          # Material3 Theme and Color Palette
└── MainActivity       # App Entry Point & Auth State Logic
 Hero Logic (The "Easter Egg")
To protect user privacy and add a fun element, ChronoKan uses a custom hashing algorithm on the Firebase UID to assign every user a permanent Superhero name (e.g., Shadow Titan or Iron Nova). This identity is consistent across sessions but unique to the user's account.
