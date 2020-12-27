package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val playerDao: PlayerDao) {

    val allPlayers: Flow<List<Player>> = playerDao.getAlphabetizedPlayers()
    val homePlayerId: Flow<Int> = playerDao.getHomePlayerId()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(player: Player) {
        playerDao.insertAll(player)
    }
}