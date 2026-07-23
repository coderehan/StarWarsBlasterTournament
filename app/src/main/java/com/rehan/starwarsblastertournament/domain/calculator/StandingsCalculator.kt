package com.rehan.starwarsblastertournament.domain.calculator

import com.rehan.starwarsblastertournament.domain.model.PlayerStanding
import com.rehan.starwarsblastertournament.data.model.Match
import com.rehan.starwarsblastertournament.data.model.Player
import javax.inject.Inject

// Turns the raw list of matches into a sorted points table.
// Rules: win = 3 pts, loss = 0 pts, draw = 1 pt each.
// Sort: highest points first; if two players have equal points, the one with the
// higher total score (sum of all their match scores) ranks higher.
class StandingsCalculator @Inject constructor() {

    fun calculateStandings(
        players: List<Player>,
        matches: List<Match>
    ): List<PlayerStanding> {

        // One running total (points + totalScore) per player, keyed by player id
        val standingsMap = players.associate { player ->
            player.id to StandingStats()
        }.toMutableMap()

        matches.forEach { match ->

            val player1Stats = standingsMap.getValue(match.player1.id)
            val player2Stats = standingsMap.getValue(match.player2.id)

            // Every match score counts towards the player's running total score,
            // win or lose — this total is only used as a tiebreaker for points.
            player1Stats.totalScore += match.player1.score
            player2Stats.totalScore += match.player2.score

            // Award points based on who scored higher in this match
            when {
                match.player1.score > match.player2.score -> {
                    player1Stats.points += 3
                }

                match.player2.score > match.player1.score -> {
                    player2Stats.points += 3
                }

                else -> {
                    // Tie — both players get 1 point
                    player1Stats.points += 1
                    player2Stats.points += 1
                }
            }
        }

        return players
            .map { player ->
                val stats = standingsMap.getValue(player.id)

                PlayerStanding(
                    player = player,
                    points = stats.points,
                    totalScore = stats.totalScore
                )
            }
            .sortedWith(
                compareByDescending<PlayerStanding> { it.points }       // 1st: most points
                    .thenByDescending { it.totalScore }                 // 2nd: tiebreaker
            )
    }

    // Small internal helper to accumulate a player's points and total score
    // while looping through matches
    private data class StandingStats(
        var points: Int = 0,
        var totalScore: Int = 0
    )
}