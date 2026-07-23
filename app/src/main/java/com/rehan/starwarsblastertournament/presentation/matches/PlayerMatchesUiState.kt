package com.rehan.starwarsblastertournament.presentation.matches

import com.rehan.starwarsblastertournament.domain.model.PlayerMatchResult

// What the Player Matches screen needs: loading/error state, the player's name
// (for the top bar title), and their list of matches, newest first.
data class PlayerMatchesUiState(
    val isLoading: Boolean = true,
    val playerName: String = "",
    val matches: List<PlayerMatchResult> = emptyList(),
    val error: String? = null
)