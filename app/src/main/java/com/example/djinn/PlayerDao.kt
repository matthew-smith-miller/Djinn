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
    fun getPlayerByName(name: String): Flow<Player>

    @Query("SELECT * FROM Player WHERE name = :name")
    fun getPlayerByNameAsPlayer(name: String): Player

    @Query("SELECT * FROM Player WHERE id = :id")
    fun getPlayerById(id: Int): Flow<Player>

    @Query("SELECT id, name FROM Player WHERE id IN (:ids)")
    fun getPlayerIdAndNames(ids: List<Int>): Flow<List<DataClasses.IdNameTuple>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg players: Player)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player): Long

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)

    @Query("DELETE FROM Player")
    suspend fun deleteAll()
}
