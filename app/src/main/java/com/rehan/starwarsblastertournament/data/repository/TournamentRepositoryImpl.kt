package com.rehan.starwarsblastertournament.data.repository

import com.rehan.starwarsblastertournament.data.local.AssetJsonDataSource
import com.rehan.starwarsblastertournament.data.model.Match
import com.rehan.starwarsblastertournament.data.model.Player
import javax.inject.Inject

// Real implementation of TournamentRepository, backed by the bundled JSON assets.
//
// FIX: players/matches are now loaded only ONCE and cached in memory using "by lazy".
// Before this fix, every call to getPlayers()/getMatches() re-opened and re-parsed the
// JSON files — so switching between Points Table and Player Matches screens kept
// re-reading the same files from disk for no reason. Since this class is bound as
// @Singleton in RepositoryModule, the lazy value survives for the whole app lifetime.
class TournamentRepositoryImpl @Inject constructor(
    private val assetJsonDataSource: AssetJsonDataSource
) : TournamentRepository {

    private val cachedPlayers: List<Player> by lazy {
        assetJsonDataSource.getPlayers()
    }

    private val cachedMatches: List<Match> by lazy {
        assetJsonDataSource.getMatches()
    }

    override fun getPlayers(): List<Player> = cachedPlayers

    override fun getMatches(): List<Match> = cachedMatches
}