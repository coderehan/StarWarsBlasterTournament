package com.rehan.starwarsblastertournament.data.repository


import com.rehan.starwarsblastertournament.data.model.Match
import com.rehan.starwarsblastertournament.data.model.Player

// Contract for getting tournament data. The rest of the app (ViewModels) only knows about
// this interface, not about JSON files or assets — so the data source could be swapped
// later (e.g. for a network API) without touching any other layer.
interface TournamentRepository {

    fun getPlayers(): List<Player>

    fun getMatches(): List<Match>
}