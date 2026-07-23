package com.rehan.starwarsblastertournament.presentation.pointstable

import com.rehan.starwarsblastertournament.domain.model.PlayerStanding

// What the Points Table screen needs to draw itself: are we loading, did something
// fail, or do we have a sorted list of standings to show.
data class PointsTableUiState(
    val isLoading: Boolean = true,
    val standings: List<PlayerStanding> = emptyList(),
    val error: String? = null
)