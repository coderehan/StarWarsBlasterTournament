# Star Wars Blaster Tournament ⚔️

An Android app built with **Jetpack Compose** that curates the results of Jabba the Hutt's
galaxy-wide Blaster tournament — settling, once and for all, whether Darth Vader really
won fair and square.

---

## Problem Statement

A long time ago, in a galaxy far, far away… all the mighty heroes took a break from the
long war to compete in a Blasters duel organized by Jabba the Hutt.

**Tournament rules:**
- Every player plays every other player once (round-robin).
- Each match, both players shoot at a target and are scored out of 100.
- The player with the **higher score** wins and is awarded **3 points**.
- The player with the **lower score** loses and is awarded **0 points**.
- If both scores are **equal (a draw)**, both players are awarded **1 point** each.

Darth Vader emerged as the tournament's ultimate winner, but rumors are flying that he
used the Dark Side to rig the results. This app turns the raw match data into a
transparent points table and per-player match history, so anyone can verify the results
for themselves.

### Screens

**1. Points Table Screen**
- Shows every player's total points, calculated from all matches played.
- Sorted in **descending order of points**.
- Ties are broken by **descending order of total score** across all matches played.

**2. Player Matches Screen**
- Reached by tapping a player on the Points Table screen.
- Shows every match that player took part in, with the **most recent match at the top**
  and the **oldest match at the bottom**.
- Shows the actual scores for both players in each match.
- Each row is color-coded from the selected player's perspective:
  - 🟢 **Green** — Win
  - 🔴 **Red** — Loss
  - ⚪ **White** — Draw

### Input Data

The app is fully offline and reads two bundled JSON files from `app/src/main/assets/`:

| File | Contents |
|---|---|
| `star_wars_players.json` | Player `id`, `name`, and `icon` URL |
| `star_wars_matches.json` | Match number and both players' `id` + `score` |

---

## Architecture

The app follows **MVVM**, layered like Clean Architecture (data / domain / presentation)
without splitting into separate Gradle modules or introducing a UseCase class for every
single action. Given the scope — two static JSON files, two screens, no network writes,
no swappable data sources — full multi-module Clean Architecture would add ceremony
without adding value. What's kept from Clean Architecture is the part that actually pays
off: **domain logic is pure, testable, and has no dependency on Android or the UI.**

```
┌─────────────────────────────────────────────────────────────────┐
│                        Presentation Layer                        │
│  (Jetpack Compose UI + ViewModel — Android/lifecycle aware)      │
│                                                                    │
│   ┌────────────────────────┐        ┌──────────────────────────┐│
│   │   Points Table Screen   │        │   Player Matches Screen  ││
│   │  PointsTableScreen.kt   │  nav   │  PlayerMatchesScreen.kt  ││
│   │  PointsTableViewModel   │───────▶│  PlayerMatchesViewModel  ││
│   │  PointsTableUiState     │        │  PlayerMatchesUiState    ││
│   └───────────┬─────────────┘        └────────────┬─────────────┘│
└───────────────┼────────────────────────────────────┼─────────────┘
                │                                    │
                ▼                                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                          Domain Layer                            │
│        (Pure Kotlin — no Android imports, unit-testable)         │
│                                                                    │
│   ┌────────────────────────┐        ┌──────────────────────────┐│
│   │   StandingsCalculator   │        │  PlayerMatchHistoryBuilder││
│   │  matches → sorted       │        │  playerId → chronological ││
│   │  points table           │        │  win/loss/draw history    ││
│   └────────────────────────┘        └──────────────────────────┘│
│                                                                    │
│   Domain models: PlayerStanding · PlayerMatchResult · MatchResult│
└───────────────────────────────┬───────────────────────────────────┘
                                 │  (Player, Match — via Repository)
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                           Data Layer                              │
│                                                                    │
│   ┌──────────────────────────┐      ┌───────────────────────────┐│
│   │   TournamentRepository    │      │   AssetJsonDataSource     ││
│   │   (interface)             │◀─────│   reads + parses the two  ││
│   │   TournamentRepositoryImpl│      │   bundled JSON files      ││
│   │   (@Singleton, caches     │      │   (Gson)                  ││
│   │   parsed data in memory)  │      └───────────────────────────┘│
│   └──────────────────────────┘                                   │
│                                                                    │
│   Data models: Player · Match · MatchPlayer                      │
└─────────────────────────────────────────────────────────────────┘
                                 ▲
                                 │
                    app/src/main/assets/
              star_wars_players.json · star_wars_matches.json
```

**Dependency direction:** Presentation → Domain → Data. Domain never depends on
Presentation or Data implementation details — it only works with the data models
handed to it and returns plain Kotlin data classes back.

### Why not full Clean Architecture (separate Gradle modules + UseCases)?

There are only two computations in this app: build a points table, and build one
player's match history. Wrapping each in its own `UseCase` class would just add a
pass-through layer around `StandingsCalculator`/`PlayerMatchHistoryBuilder` without
changing behavior or testability — both calculators are already pure, injectable,
and unit-testable on their own. The one piece of Clean Architecture kept deliberately
is the **`TournamentRepository` interface**, since it's a genuinely cheap way to keep
ViewModels testable against a fake, independent of how the data is actually sourced.

### Layer responsibilities

| Layer | Responsibility | Depends on |
|---|---|---|
| **Data** | Read raw JSON, expose `Player`/`Match` via a cached, singleton repository | Android `Context` (for assets), Gson |
| **Domain** | Pure business rules: scoring, sorting, tiebreakers, win/loss/draw, match ordering | Nothing (plain Kotlin) |
| **Presentation** | Compose UI + `ViewModel`s exposing `StateFlow<UiState>` per screen | Domain, Data (via interface), Android lifecycle |

---

## Tech Stack

- **Kotlin** — 100% Kotlin, idiomatic naming conventions throughout
- **Jetpack Compose** — declarative UI, Material 3
- **Jetpack Navigation for Compose** — single-activity navigation between the two screens
- **Hilt** — dependency injection (`@HiltAndroidApp`, `@HiltViewModel`, `@Binds`, `@Singleton`)
- **Kotlin Coroutines + Flow** — `StateFlow` for UI state, `Dispatchers.IO` for parsing
- **Gson** — JSON parsing of bundled assets
- **Coil** — async image loading for player icons

---

## Project Structure

```
app/src/main/java/com/rehan/starwarsblastertournament/
├── data/
│   ├── model/              # Player, Match, MatchPlayer — raw JSON shapes
│   ├── local/               # AssetJsonDataSource — reads assets
│   └── repository/          # TournamentRepository (interface) + Impl
├── di/
│   └── RepositoryModule.kt  # Hilt bindings
├── domain/
│   ├── model/               # PlayerStanding, PlayerMatchResult, MatchResult
│   └── calculator/          # StandingsCalculator, PlayerMatchHistoryBuilder
├── presentation/
│   ├── pointstable/
│   │   ├── components/      # Reusable row composables
│   │   ├── PointsTableScreen.kt
│   │   ├── PointsTableViewModel.kt
│   │   └── PointsTableUiState.kt
│   ├── matches/
│   │   ├── components/      # PlayerMatchResultItem.kt
│   │   ├── PlayerMatchesScreen.kt
│   │   ├── PlayerMatchesViewModel.kt
│   │   └── PlayerMatchesUiState.kt
│   └── navigation/
│       ├── Destination.kt
│       └── AppNavHost.kt
├── StarWarsApplication.kt
└── MainActivity.kt

app/src/main/assets/
├── star_wars_players.json
└── star_wars_matches.json

app/src/main/res/xml/
└── network_security_config.xml   # allows icon loading from icons.iconarchive.com
```

---

## Core Logic

### Points Table calculation (`StandingsCalculator`)

1. Every player starts with `0` points and `0` total score.
2. For each match: award 3/0 points on a win/loss, or 1/1 on a draw; add each player's
   match score to their running total score.
3. Sort descending by `points`, then descending by `totalScore` as a tiebreaker.

### Player Match History (`PlayerMatchHistoryBuilder`)

1. Filter matches down to the ones the selected player took part in.
2. Sort **descending by match number** (higher number = played more recently).
3. Normalize each match so the selected player is always shown as the "left" player and
   their opponent as the "right" player, regardless of which side they were on in the
   raw data.
4. Compute `MatchResult` (WIN / LOSS / DRAW) from the selected player's perspective, which
   the UI maps to green / red / white.

---

## Screenshots

<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/e2af6c1f-9103-4550-8a64-74805c4e3c93" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/89db7820-aa8b-4883-a911-5e658a010832" />
<img width=30% height=40% alt="Image" src="https://github.com/user-attachments/assets/d59a1262-71d6-4302-a59f-bb95c073afdf" />
