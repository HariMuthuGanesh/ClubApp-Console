# ClubApp — Dynamic Club Management System

> A Java console application for managing student clubs, events, and memberships across three user roles: **Admin**, **Coordinator**, and **Student**.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Features](#2-features)
3. [Project Structure](#3-project-structure)
4. [Architecture & Design](#4-architecture--design)
5. [Class Reference](#5-class-reference)
6. [Data Storage](#6-data-storage)
7. [User Roles & Permissions](#7-user-roles--permissions)
8. [Getting Started](#8-getting-started)
9. [Default Credentials](#9-default-credentials)
10. [Input Validation Rules](#10-input-validation-rules)
11. [Application Flow](#11-application-flow)

---

## 1. Project Overview

**ClubApp** is a console-based Java application that enables institutions to manage student clubs end-to-end. It supports three roles with different permission levels, persists all data using flat text files, and enforces input validation throughout.

| Attribute     | Value                         |
|---------------|-------------------------------|
| Language      | Java (SE)                     |
| UI Type       | Console / CLI                 |
| Persistence   | Flat file (pipe-delimited TXT)|
| Build Tool    | javac (manual compilation)    |
| Entry Point   | `clubApp.java`                |

---

## 2. Features

- **Role-based access control** — Admin, Coordinator, Student with distinct menus and capabilities
- **Club management** — Create, list, and delete clubs (Admin only)
- **Event management** — Add, update, remove events within a club (Coordinator & Admin)
- **Membership** — Any user can join clubs; duplicate prevention enforced
- **Event registration** — Users can register for specific events; duplicate registration is blocked
- **Input validation** — Email format and password strength enforced at registration
- **File persistence** — All users, clubs, events, memberships, and registrations are saved to and loaded from the `file/` directory automatically
- **Duplicate prevention** — Clubs and events with duplicate names are rejected

---

## 3. Project Structure

```
ClubApp/
├── clubApp.java                  # Application entry point & all menus
│
├── clubs/                        # Club & event domain logic
│   ├── Club.java                 # Club entity with members and events
│   ├── ClubManager.java          # CRUD + file I/O for clubs
│   └── Event.java                # Event entity with attendee tracking
│
├── manage/                       # User domain logic
│   ├── Person.java               # Base class: name, email, password
│   ├── User.java                 # Extends Person (Coordinator / Student)
│   ├── Admin.java                # Extends Person (Admin role)
│   └── UserManager.java          # CRUD + file I/O for users
│
├── details/                      # Registration helpers (input collection)
│   ├── AdminDetails.java         # Admin registration form
│   ├── CoordinatorDetails.java   # Coordinator registration form
│   └── StudentDetails.java       # Student registration form
│
├── validator/
│   └── Validator.java            # Email & password regex validators
│
└── file/                         # Persistent data store (auto-created)
    ├── users.txt                 # All user accounts (role|name|email|password)
    ├── clubs.txt                 # Club records (name|description|coordinatorEmail)
    ├── events.txt                # Event records (club|name|desc|venue|date)
    ├── members.txt               # Club memberships (club|memberEmail)
    └── event_registrations.txt   # Event sign-ups (club|event|attendeeEmail)
```

---

## 4. Architecture & Design

### Package Diagram

```
┌─────────────────────────────────────────────────────┐
│                    clubApp.java                     │
│         (Main · Menus · Navigation Logic)           │
└──────┬──────────────────────┬───────────────────────┘
       │                      │
       ▼                      ▼
┌─────────────┐       ┌──────────────┐
│   manage/   │◄──────│   details/   │
│  Person     │       │ AdminDetails │
│  User       │       │ CoordDetails │
│  Admin      │       │ StudentDets  │
│  UserManager│       └──────┬───────┘
└──────┬──────┘              │
       │                     ▼
       │              ┌─────────────┐
       │              │  validator/ │
       │              │  Validator  │
       │              └─────────────┘
       ▼
┌─────────────┐
│   clubs/    │
│  Club       │
│  Event      │
│  ClubManager│
└──────┬──────┘
       │
       ▼
 ┌───────────┐
 │  file/    │  ← Flat-file persistence layer
 │  *.txt    │
 └───────────┘
```

### Design Patterns Used

| Pattern | Where Applied |
|---|---|
| **Inheritance** | `User` and `Admin` both extend `Person` |
| **Encapsulation** | All entity fields are `private` with getters/setters |
| **Separation of Concerns** | Registration logic separated into `details/` package |
| **Manager Pattern** | `UserManager` and `ClubManager` centralise all CRUD and persistence |
| **Role-based Access** | `clubInteractionMenu()` uses `instanceof` + `fullAccess` flag |

---

## 5. Class Reference

### `clubApp` *(default package)*

The application's main class and controller. Owns both `UserManager` and `ClubManager` instances and orchestrates all navigation menus.

| Method | Description |
|---|---|
| `main(String[])` | Bootstraps the system, seeds default users, runs the top-level loop |
| `handleLogin()` | Prompts role selection → credential validation → routes to appropriate menu |
| `handleRegistration()` | Routes to the correct `Details.register()` helper by role |
| `adminMenu(Admin)` | Admin-specific menu: create club, view/manage, delete |
| `coordinatorMenu(User)` | Directly enters club interaction for the coordinator's assigned club |
| `studentMenu(User)` | Student menu: browse clubs, join, register for events |
| `clubInteractionMenu(Person, Club, boolean)` | Shared interaction hub; `fullAccess=true` unlocks event management |

---

### `clubs` Package

#### `Club`

Represents a club entity.

| Field | Type | Description |
|---|---|---|
| `clubName` | `String` | Unique name of the club |
| `description` | `String` | Short club description |
| `coordinator` | `User` | Assigned coordinator |
| `events` | `List<Event>` | Events scheduled by this club |
| `members` | `List<Person>` | All joined members |

| Method | Description |
|---|---|
| `addEvent(Event)` | Adds event; rejects duplicates by name (case-insensitive) |
| `removeEvent(int)` | Removes event at given index |
| `updateEvent(int, Event)` | Replaces event at given index with new data |
| `listEvents()` | Prints all events to console |
| `viewDetails()` | Prints club name, description, coordinator, and event count |
| `addMember(Person)` | Adds a member to the club |
| `isMember(Person)` | Returns `true` if the person is already a member (email match) |
| `registerStudentForEvent(Person, int)` | Registers user for an event; prevents duplicate registration |

---

#### `Event`

Represents a scheduled club event.

| Field | Type | Description |
|---|---|---|
| `name` | `String` | Event name (must be unique per club) |
| `description` | `String` | What the event is about |
| `venue` | `String` | Location of the event |
| `date` | `String` | Date of the event (free-form string) |
| `attendees` | `List<Person>` | People registered for this event |

| Method | Description |
|---|---|
| `addAttendee(Person)` | Adds attendee; skips if already registered |
| `isRegistered(Person)` | Checks registration by email match |
| `toString()` | Formatted string for console display |

---

#### `ClubManager`

Central service for all club operations and file persistence.

| Method | Description |
|---|---|
| `addClub(Club)` | Adds club; rejects duplicates by name; triggers `saveAll()` |
| `removeClub(int)` | Removes club at index; triggers `saveAll()` |
| `listAllClubs()` | Prints numbered list of all clubs |
| `findClubByName(String)` | Case-insensitive club lookup by name |
| `getClubByCoordinator(User)` | Finds the club managed by a given coordinator |
| `getClubByIndex(int)` | Safe index-based club retrieval |
| `getClubCount()` | Returns total number of clubs |
| `listJoinedClubs(Person)` | Lists clubs where the given person is a member |
| `joinClub(Person, int)` | Joins a club; prevents duplicate membership |
| `saveAll()` | Writes all clubs, events, members, registrations to `file/` |
| `loadAll(UserManager)` | Reads and reconstructs all data from `file/` on startup |

---

### `manage` Package

#### `Person` *(abstract base)*

Base class for all user types.

| Field | Type |
|---|---|
| `name` | `String` |
| `email` | `String` |
| `password` | `String` |

#### `User extends Person`

Used for both **Coordinator** and **Student** roles. No additional fields beyond `Person`.

#### `Admin extends Person`

Used for the **Admin** role. No additional fields beyond `Person`. Distinguished by `instanceof Admin` at runtime.

---

#### `UserManager`

Manages all user accounts and persistence to `file/users.txt`.

| Method | Description |
|---|---|
| `addAdmin(Admin)` | Adds admin and saves |
| `addCoordinator(User)` | Adds coordinator and saves |
| `addStudent(User)` | Adds student and saves |
| `validateAdmin(email, password)` | Returns `Admin` if credentials match, else `null` |
| `validateCoordinator(email, password)` | Returns `User` if credentials match |
| `validateStudent(email, password)` | Returns `User` if credentials match |
| `findAdminByEmail(String)` | Lookup admin by email |
| `findCoordinatorByEmail(String)` | Lookup coordinator by email |
| `findStudentByEmail(String)` | Lookup student by email |

---

### `details` Package

Standalone static helper classes that handle console-based registration forms using `Scanner`. Each class:
1. Reads name, email (validated), and password (validated) from stdin
2. Checks for duplicate emails before proceeding
3. Creates and registers the appropriate user object

| Class | Role Registered |
|---|---|
| `AdminDetails.register(UserManager)` | Admin |
| `CoordinatorDetails.register(UserManager)` | Coordinator |
| `StudentDetails.register(UserManager)` | Student |

---

### `validator` Package

#### `Validator`

Provides static regex-based validation methods.

| Method | Regex Rule |
|---|---|
| `isValidEmail(String)` | `^[A-Za-z0-9+_.-]+@(.+)$` |
| `isValidPassword(String)` | Min 8 chars, ≥1 uppercase, ≥1 lowercase, ≥1 digit, ≥1 special char (`@#$%^&+=`), no whitespace |

---

## 6. Data Storage

All data is persisted in pipe-delimited (`|`) plain text files in the `file/` directory. Files are created automatically on first run.

### `file/users.txt`

```
ADMIN|Hari|hari@gmail.com|Hari@123
COORD|Muthu|muthu@gmail.com|Muthu@123
STUDENT|Ganesh|ganesh@gmail.com|Ganesh@123
```

### `file/clubs.txt`

```
<ClubName>|<Description>|<CoordinatorEmail>
```

### `file/events.txt`

```
<ClubName>|<EventName>|<Description>|<Venue>|<Date>
```

### `file/members.txt`

```
<ClubName>|<MemberEmail>
```

### `file/event_registrations.txt`

```
<ClubName>|<EventName>|<AttendeeEmail>
```

> **Note:** The `file/` directory and all `.txt` files must exist (or be creatable) before launching the application. They are auto-written on first data change.

---

## 7. User Roles & Permissions

| Feature | Admin | Coordinator | Student |
|---|:---:|:---:|:---:|
| Create a Club | ✅ | ❌ | ❌ |
| Delete a Club | ✅ | ❌ | ❌ |
| View All Clubs | ✅ | ✅ | ✅ |
| View Club Details | ✅ | ✅ | ✅ |
| Join a Club | ✅ | ✅ | ✅ |
| View Events | ✅ | ✅ | ✅ |
| Register for Event | ✅ | ✅ | ✅ |
| Add Event | ✅ | ✅ (own club) | ❌ |
| Remove Event | ✅ | ✅ (own club) | ❌ |
| Update Event | ✅ | ✅ (own club) | ❌ |
| View Joined Clubs | ✅ | ❌ | ✅ |

> Coordinators access only the club they are assigned to by the Admin. They cannot switch between clubs.

---

## 8. Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Command Prompt or PowerShell (Windows) / Terminal (Linux/macOS)

### Compile

Run from the **root** of the project (`d:\ClubApp`):

```powershell
javac -d . clubApp.java clubs\*.java manage\*.java details\*.java validator\*.java
```

### Run

```powershell
java clubApp
```

> Ensure the `file/` directory exists before the first run, or let the app create files on its first write operation.

---

## 9. Default Credentials

The application seeds three default accounts on first launch (only if they do not already exist in `users.txt`):

| Role | Name | Email | Password |
|---|---|---|---|
| Admin | Hari | hari@gmail.com | Hari@123 |
| Coordinator | Muthu | muthu@gmail.com | Muthu@123 |
| Student | Ganesh | ganesh@gmail.com | Ganesh@123 |

---

## 10. Input Validation Rules

### Email
- Must match the pattern: `[chars]@[domain]`
- Example valid: `user@example.com`

### Password
- Minimum **8 characters**
- At least one **uppercase** letter (`A–Z`)
- At least one **lowercase** letter (`a–z`)
- At least one **digit** (`0–9`)
- At least one **special character** (`@`, `#`, `$`, `%`, `^`, `&`, `+`, `=`)
- **No whitespace** allowed

---

## 11. Application Flow

```
Start
  │
  ├─► Load users.txt → Seed default accounts if missing
  ├─► Load clubs.txt, events.txt, members.txt, event_registrations.txt
  │
  └─► Main Menu
        ├─ 1. Login
        │     ├─ Admin    → adminMenu()
        │     │               ├─ Create Club
        │     │               ├─ View All Clubs → clubInteractionMenu() [fullAccess=true]
        │     │               ├─ View Joined Clubs
        │     │               └─ Delete Club
        │     │
        │     ├─ Coordinator → coordinatorMenu()
        │     │                  └─ clubInteractionMenu() for assigned club [fullAccess=true]
        │     │
        │     └─ Student  → studentMenu()
        │                     ├─ View All Clubs → clubInteractionMenu() [fullAccess=false]
        │                     └─ View Joined Clubs
        │
        ├─ 2. Register
        │     ├─ Admin    → AdminDetails.register()
        │     ├─ Coordinator → CoordinatorDetails.register()
        │     └─ Student  → StudentDetails.register()
        │
        └─ 3. Exit

clubInteractionMenu(person, club, fullAccess)
  ├─ 1. View Club Details
  ├─ 2. View Events
  ├─ 3. Join Club
  ├─ 4. Register for an Event
  ├─ 5. Add Event          [Coordinator / Admin only]
  ├─ 6. Remove Event       [fullAccess only]
  ├─ 7. Update Event       [fullAccess only]
  └─ 0. Back
```

---

*Documentation generated for ClubApp — March 2026*
