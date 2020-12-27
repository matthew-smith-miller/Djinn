package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<Game>> = gameDao.getAllGames()

    fun getGamesFromRivalryId(rivalryId: Int): Flow<List<Game>> {
        return gameDao.getGamesFromRivalryId(rivalryId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(game: Game) {
        gameDao.insertAll(game)
    }
}