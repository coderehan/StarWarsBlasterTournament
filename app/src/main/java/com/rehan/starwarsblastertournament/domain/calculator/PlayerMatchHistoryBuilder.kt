package com.rehan.starwarsblastertournament.domain.calculator

import com.rehan.starwarsblastertournament.domain.model.MatchResult
import com.rehan.starwarsblastertournament.domain.model.PlayerMatchResult
import com.rehan.starwarsblastertournament.data.model.Match
import com.rehan.starwarsblastertournament.data.model.Player
import javax.inject.Inject

// Builds the full match history for ONE player, for the Player Matches screen.
// Always puts the requested player on the "left" and their opponent on the "right",
// so the UI doesn't need to know whether they were player1 or player2 in the raw data.
class PlayerMatchHistoryBuilder @Inject constructor() {

    fun buildMatchHistory(
        playerId: Int,
        players: List<Player>,
        matches: List<Match>
    ): List<PlayerMatchResult> {

        val playersMap = players.associateBy { it.id }

        return matches
            // Keep only matches this player took part in
            .filter { match ->
                match.player1.id == playerId || match.player2.id == playerId
            }
            // Most recent match first — assumes higher "match" number = played later
            .sortedByDescending { it.match }
            .map { match ->

                val isPlayerOne = match.player1.id == playerId

                // Figure out which side of the raw match data is "our" player
                // vs. the opponent, so the screen always shows them consistently
                val selectedPlayer =
                    if (isPlayerOne) playersMap.getValue(match.player1.id)
                    else playersMap.getValue(match.player2.id)

                val opponent =
                    if (isPlayerOne) playersMap.getValue(match.player2.id)
                    else playersMap.getValue(match.player1.id)

                val selectedPlayerScore =
                    if (isPlayerOne) match.player1.score else match.player2.score

                val opponentScore =
                    if (isPlayerOne) match.player2.score else match.player1.score

                // Decide win/loss/draw from the selected player's perspective
                val result = when {
                    selectedPlayerScore > opponentScore -> MatchResult.WIN
                    selectedPlayerScore < opponentScore -> MatchResult.LOSS
                    else -> MatchResult.DRAW
                }

                PlayerMatchResult(
                    matchNumber = match.match,
                    leftPlayer = selectedPlayer,
                    rightPlayer = opponent,
                    leftScore = selectedPlayerScore,
                    rightScore = opponentScore,
                    result = result
                )
            }
    }
}