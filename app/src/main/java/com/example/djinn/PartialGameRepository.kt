package com.example.djinn

class PartialGameRepository(
    private val partialGameDao: PartialGameDao
) {

    /*@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(partialGame: PartialGame) {
        partialGameDao.insert(partialGame)
        gameRepository.incrementScore(partialGame)
    }*/

    @Suppress("RedundantSuspendModifier")
    suspend fun insert(partialGames: List<PartialGame>): List<Long> {
        return partialGameDao.insert(partialGames)
    }
}