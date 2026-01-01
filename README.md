# ChronoKan

An Android Kanban board application built with Kotlin and Jetpack Compose that supports task state history traversal using a time-indexed board snapshot mechanism.

---

## Overview

ChronoKan is a productivity-oriented Android application that implements a Kanban-style workflow with persistent historical state tracking. In addition to standard task movement across columns, the application allows users to navigate through previous board states to analyze task evolution over time.

The project is built using modern Android development principles with a focus on maintainable architecture, predictable state management, and offline-first persistence.

---

## Architecture

ChronoKan follows the **MVVM (Model–View–ViewModel)** architecture pattern to ensure separation of concerns and scalability.

- **UI Layer**: Declarative UI built with Jetpack Compose
- **ViewModel Layer**: State management and business logic
- **Data Layer**: Local persistence using Room with repository abstraction

The application uses a unidirectional data flow model with coroutine-based asynchronous execution.

---

## Features

- Kanban workflow with Todo, In Progress, and Done columns
- Time-indexed board state history traversal
- Offline-first task persistence using Room
- Firebase Authentication (Email/Password and Google Sign-In)
- Fully declarative UI with Jetpack Compose
- Reactive state updates using Kotlin coroutines

---

## Project Structure

com.example.chronokan
├── data
│ ├── database # Room database configuration
│ ├── dao # Data access objects
│ └── repository # Data repositories
├── ui
│ ├── auth # LoginScreen, ProfileScreen, AuthViewModel
│ ├── board # TaskListScreen, BoardViewModel
│ ├── navigation # ChronosNavGraph
│ └── theme # Material 3 theme and color palette
└── MainActivity # Application entry point and auth state handling

## Tech Stack

- **Language**: Kotlin  
- **UI Framework**: Jetpack Compose (Material 3)  
- **Architecture**: MVVM  
- **Local Persistence**: Room Database  
- **Authentication**: Firebase Auth  
- **Asynchronous Processing**: Kotlin Coroutines  
- **Navigation**: Compose Navigation  

---

## Future Scope

- Cross-device sync
- Board export and analytics
- Multi-board support
- Cloud-backed collaboration features

---
