package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM Game")
    fun getAllGames(): Flow<List<Game>>

    @Transaction
    @Query("SELECT * FROM Game")
    fun getAllGamesWithPartialGames(): Flow<List<DataClasses.GameWithPartialGames>>

    @Query("SELECT * FROM Game where rivalry = :rivalryId")
    fun getGamesFromRivalryId(rivalryId: Int): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg games: Game)

    @Delete
    suspend fun delete(game: Game)
}