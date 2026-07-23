package com.rehan.starwarsblastertournament.di

import com.rehan.starwarsblastertournament.data.repository.TournamentRepository
import com.rehan.starwarsblastertournament.data.repository.TournamentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Tells Hilt: "whenever something asks for a TournamentRepository, give it a
// TournamentRepositoryImpl". @Singleton means one shared instance for the whole app,
// which is what makes the caching fix above actually work.
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTournamentRepository(
        repositoryImpl: TournamentRepositoryImpl
    ): TournamentRepository
}