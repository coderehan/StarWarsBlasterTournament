package com.rehan.starwarsblastertournament.presentation.navigation

// Type-safe route definitions, so route strings aren't scattered/hardcoded everywhere.
sealed class Destination(val route: String) {

    data object PointsTable : Destination("points_table")

    // "player_matches/{playerId}" — {playerId} is a placeholder filled in via createRoute()
    data object PlayerMatches : Destination("player_matches/{playerId}") {

        fun createRoute(playerId: Int): String {
            return "player_matches/$playerId"
        }
    }
}