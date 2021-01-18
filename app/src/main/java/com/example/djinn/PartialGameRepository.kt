package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class PartialGameRepository(
    private val partialGameDao: PartialGameDao,
    private val gameRepository: GameRepository
) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(partialGame: PartialGame) {
        partialGameDao.insertAll(partialGame)
        gameRepository.incrementScore(partialGame)
    }
}