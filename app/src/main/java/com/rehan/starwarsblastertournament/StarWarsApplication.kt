package com.rehan.starwarsblastertournament

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Required for Hilt to work — sets up the dependency graph for the whole app.
@HiltAndroidApp
class StarWarsApplication : Application()