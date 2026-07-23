package com.rehan.starwarsblastertournament.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rehan.starwarsblastertournament.data.model.Match
import com.rehan.starwarsblastertournament.data.model.Player
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Reads the two JSON files from the app's assets folder and turns them into Kotlin objects.
// This is the only place in the app that touches raw files — everything else works with
// clean Player/Match objects.

class AssetJsonDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val gson = Gson()

    // Reads star_wars_players.json and converts it to a List<Player>
    fun getPlayers(): List<Player> {
        val json = context.assets
            .open("star_wars_players.json")
            .bufferedReader()
            .use { it.readText() }  // "use" makes sure the file stream is closed automatically

        val type = object : TypeToken<List<Player>>() {}.type

        return gson.fromJson(json, type)
    }

    // Reads star_wars_matches.json and converts it to a List<Match>
    fun getMatches(): List<Match> {
        val json = context.assets
            .open("star_wars_matches.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Match>>() {}.type

        return gson.fromJson(json, type)
    }
}