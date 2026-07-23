package com.rehan.starwarsblastertournament.data.model

// A single blaster match between two players, as it appears in star_wars_matches.json
data class Match(
    val match: Int,              // match number — also used as chronological order
    val player1: MatchPlayer,
    val player2: MatchPlayer
)