package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class PartialGameRepository(private val partialGameDao: PartialGameDao) {

    val allPartialGames: Flow<List<PartialGame>> = partialGameDao.getAllPartialGames()

    fun getPartialGamesFromGameId(gameId: Int): Flow<List<PartialGame>> {
        return partialGameDao.getPartialGamesFromGameId(gameId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(partialGame: PartialGame) {
        partialGameDao.insertAll(partialGame)
    }
}