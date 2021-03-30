package com.example.djinn

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class RivalryRepository(private val rivalryDao: RivalryDao) {

    val allRivalries: Flow<List<Rivalry>> = rivalryDao.getAllRivalries()
    val activeRivalries: Flow<List<Rivalry>> = rivalryDao.getActiveRivalries()
    fun getRivalryWithGamesById(id: Int): Flow<DataClasses.RivalryWithGames> {
        return rivalryDao.getRivalryWithGamesById(id)
    }

    fun getRivalryById(id: Int): Flow<Rivalry> {
        return rivalryDao.getRivalryById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @UiThread
    suspend fun incrementScore(game: Game) {
        getRivalryById(game.rivalry).collect { rivalry ->
            rivalry.visitorScore += game.visitorScore
            rivalry.homeScore += game.homeScore
            update(rivalry)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @UiThread
    suspend fun rollupScore(rivalryIds: List<Int>) {
        val rivalriesToUpdate = mutableListOf<Rivalry>()
        val rivalriesWithGames = rivalryDao.getRivalryWithGamesById(rivalryIds)
        for (rivalryWithGames in rivalriesWithGames) {
            var visitorScore = 0
            var homeScore = 0
            for (game in rivalryWithGames.games) {
                if (game.status == "Completed") {
                    visitorScore += game.visitorScore
                    homeScore += game.homeScore
                }
            }
            rivalryWithGames.rivalry.visitorScore = visitorScore
            rivalryWithGames.rivalry.homeScore = homeScore
            rivalriesToUpdate.add(rivalryWithGames.rivalry)
        }
        rivalryDao.update(rivalriesToUpdate)
    }

    @Suppress("RedundantSuspendModifier")
    @UiThread
    suspend fun insert(rivalry: Rivalry) {
        rivalryDao.insertAll(rivalry)
    }

    @Suppress("RedundantSuspendModifier")
    @UiThread
    suspend fun update(rivalry: Rivalry) {
        rivalryDao.update(rivalry)
    }
}