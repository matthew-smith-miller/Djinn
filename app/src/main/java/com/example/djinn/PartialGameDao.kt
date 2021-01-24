package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PartialGameDao {
    @Query("SELECT * FROM PartialGame")
    fun getAllPartialGames(): Flow<List<PartialGame>>

    @Query("SELECT * FROM PartialGame WHERE game = :gameId")
    fun getPartialGamesFromGameId(gameId: Int?): Flow<List<PartialGame>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg partialGames: PartialGame)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(partialGames: List<PartialGame>): List<Long>

    @Delete
    suspend fun delete(partialGame: PartialGame)

    @Query("DELETE FROM PartialGame")
    suspend fun deleteAll()
}