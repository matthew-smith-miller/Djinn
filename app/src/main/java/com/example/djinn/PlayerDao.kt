package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    fun getAllPlayers(): Flow<List<Player>>

    @Query("SELECT * FROM Player")
    fun getAllPlayersAsList(): List<Player>

    @Query("SELECT * FROM Player WHERE name = :name")
    fun getPlayerFromName(name: String): Player

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg players: Player)

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)

    @Query("DELETE FROM Player")
    suspend fun deleteAll()
}
