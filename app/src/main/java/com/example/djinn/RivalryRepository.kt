package com.example.djinn

import androidx.annotation.WorkerThread
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RivalryRepository(private val rivalryDao: RivalryDao) {

    val allRivalries: Flow<List<Rivalry>> = rivalryDao.getAllRivalries()
    fun getRivalryWithGamesById(id: Int): Flow<DataClasses.RivalryWithGames> {
        return rivalryDao.getRivalryWithGamesById(id)
    }

    fun getRivalryById(id: Int): Rivalry {
        return rivalryDao.getRivalryById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun incrementScore(game: Game) {
        val rivalry = getRivalryById(game.rivalry)
        rivalry.visitorScore += game.visitorScore
        rivalry.homeScore += game.homeScore
        update(rivalry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(rivalry: Rivalry) {
        rivalryDao.insertAll(rivalry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(rivalry: Rivalry) {
        rivalryDao.updateAll(rivalry)
    }
}