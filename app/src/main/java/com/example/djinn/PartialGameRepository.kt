package com.example.djinn

import androidx.annotation.WorkerThread

class PartialGameRepository(
    private val partialGameDao: PartialGameDao,
    private val gameRepository: GameRepository
) {

    /*@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(partialGame: PartialGame) {
        partialGameDao.insert(partialGame)
        gameRepository.incrementScore(partialGame)
    }*/

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(partialGames: List<PartialGame>) {
        partialGameDao.insert(partialGames)
        gameRepository.rollupScore(partialGames)
    }
}