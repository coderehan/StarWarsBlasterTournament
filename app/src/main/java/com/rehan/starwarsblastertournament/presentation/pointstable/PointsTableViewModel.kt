package com.rehan.starwarsblastertournament.presentation.pointstable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.starwarsblastertournament.data.repository.TournamentRepository
import com.rehan.starwarsblastertournament.domain.calculator.StandingsCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Loads players + matches from the repository, runs them through StandingsCalculator,
// and exposes the result as UI state for PointsTableScreen to observe.
@HiltViewModel
class PointsTableViewModel @Inject constructor(
    private val repository: TournamentRepository,
    private val standingsCalculator: StandingsCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(PointsTableUiState())
    val uiState: StateFlow<PointsTableUiState> = _uiState.asStateFlow()

    init {
        // Load as soon as the ViewModel is created — the points table doesn't
        // depend on any navigation argument, so there's nothing to wait for
        loadStandings()
    }

    private fun loadStandings() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val players = repository.getPlayers()
                val matches = repository.getMatches()

                val standings = standingsCalculator.calculateStandings(
                    players = players,
                    matches = matches
                )

                _uiState.value = PointsTableUiState(
                    isLoading = false,
                    standings = standings
                )

            } catch (exception: Exception) {

                _uiState.value = PointsTableUiState(
                    isLoading = false,
                    error = exception.message
                )
            }
        }
    }
}