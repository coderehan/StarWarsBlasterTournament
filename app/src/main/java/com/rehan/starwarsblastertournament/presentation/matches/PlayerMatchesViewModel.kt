package com.rehan.starwarsblastertournament.presentation.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.starwarsblastertournament.data.repository.TournamentRepository
import com.rehan.starwarsblastertournament.domain.calculator.PlayerMatchHistoryBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Loads a single player's match history. Unlike PointsTableViewModel, this can't
// load in init{} because it needs the playerId, which only arrives after
// navigation — so the screen calls loadPlayerMatches(playerId) explicitly.
@HiltViewModel
class PlayerMatchesViewModel @Inject constructor(
    private val repository: TournamentRepository,
    private val matchHistoryBuilder: PlayerMatchHistoryBuilder
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerMatchesUiState())
    val uiState: StateFlow<PlayerMatchesUiState> = _uiState.asStateFlow()

    fun loadPlayerMatches(playerId: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val players = repository.getPlayers()
                val matches = repository.getMatches()

                val playerMatches = matchHistoryBuilder.buildMatchHistory(
                    playerId = playerId,
                    players = players,
                    matches = matches
                )

                val playerName = players.first { it.id == playerId }.name

                _uiState.value = PlayerMatchesUiState(
                    isLoading = false,
                    playerName = playerName,
                    matches = playerMatches
                )

            } catch (exception: Exception) {

                _uiState.value = PlayerMatchesUiState(
                    isLoading = false,
                    error = exception.message
                )
            }
        }
    }
}