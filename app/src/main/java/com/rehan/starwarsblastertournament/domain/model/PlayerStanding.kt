package com.rehan.starwarsblastertournament.domain.model

import com.rehan.starwarsblastertournament.data.model.Player

// One row on the Points Table screen: a player plus their total tournament points
// and total score across all their matches (used only to break ties in points).
data class PlayerStanding(
    val player: Player,
    val points: Int,
    val totalScore: Int
)