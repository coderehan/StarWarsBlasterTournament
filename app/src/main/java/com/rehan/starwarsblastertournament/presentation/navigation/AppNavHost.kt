package com.rehan.starwarsblastertournament.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rehan.starwarsblastertournament.presentation.matches.PlayerMatchesScreen
import com.rehan.starwarsblastertournament.presentation.pointstable.PointsTableScreen

// The app's single navigation graph: Points Table is the start screen; tapping a
// player pushes the Player Matches screen with that player's id as an argument.
@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.PointsTable.route
    ) {

        composable(Destination.PointsTable.route) {
            PointsTableScreen(
                onPlayerClick = { playerId ->
                    navController.navigate(Destination.PlayerMatches.createRoute(playerId))
                }
            )
        }

        composable(
            route = Destination.PlayerMatches.route,
            arguments = listOf(
                navArgument("playerId") { type = NavType.IntType }
            )
        ) {
            val playerId = it.arguments?.getInt("playerId") ?: 0
            PlayerMatchesScreen(
                playerId = playerId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}