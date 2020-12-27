package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PartialGameDao {
    @Query("SELECT * FROM PartialGame")
    fun getAllPartialGames(): Flow<List<PartialGame>>

    @Query("SELECT * FROM PartialGame WHERE game = :gameId")
    fun getPartialGamesFromGameId(gameId: Int): Flow<List<PartialGame>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg partialGames: PartialGame)

    @Delete
    suspend fun delete(partialGame: PartialGame)
}