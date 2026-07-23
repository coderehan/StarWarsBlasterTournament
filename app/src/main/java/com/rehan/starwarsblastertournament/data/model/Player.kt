package com.rehan.starwarsblastertournament.data.model

// A tournament player, as it appears in star_wars_players.json
data class Player(
    val id: Int,
    val name: String,
    val icon: String   // URL to the player's icon image
)