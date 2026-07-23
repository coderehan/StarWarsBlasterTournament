package com.rehan.starwarsblastertournament.domain.model

import com.rehan.starwarsblastertournament.data.model.Player

// One row on the Player Matches screen: always shows the *selected* player on the left
// and their opponent on the right, regardless of whether they were player1 or player2
// in the raw match data.
data class PlayerMatchResult(
    val matchNumber: Int,
    val leftPlayer: Player,    // the player whose match-history screen this is
    val rightPlayer: Player,   // their opponent for this match
    val leftScore: Int,
    val rightScore: Int,
    val result: MatchResult    // win/loss/draw from leftPlayer's point of view
)