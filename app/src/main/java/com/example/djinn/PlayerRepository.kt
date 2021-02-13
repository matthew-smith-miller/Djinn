package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val playerDao: PlayerDao) {

    val allPlayers: Flow<List<Player>> = playerDao.getAllPlayers()

    fun getPlayerByName(name: String): Flow<Player> {
        return playerDao.getPlayerByName(name)
    }

    fun getPlayerById(id: Int): Flow<Player> {
        return playerDao.getPlayerById(id)
    }

    fun getPlayerIdsAndNames(ids: List<Int>): Flow<List<DataClasses.IdNameTuple>> {
        return playerDao.getPlayerIdAndNames(ids)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(player: Player) {
        playerDao.insertAll(player)
    }
}