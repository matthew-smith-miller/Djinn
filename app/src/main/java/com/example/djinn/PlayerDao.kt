package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    fun getAlphabetizedPlayers(): Flow<List<Player>>

    @Query("SELECT id FROM Player WHERE isHomePlayer = true")
    fun getHomePlayerId(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg players: Player)

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)
}
