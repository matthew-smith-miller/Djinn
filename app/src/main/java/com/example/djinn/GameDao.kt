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

    @Query("SELECT * FROM Game WHERE rivalry = :rivalryId")
    fun getGamesFromRivalryId(rivalryId: Int): Flow<List<Game>>

    @Query("SELECT * FROM Game WHERE id = :id")
    fun getGameWithPartialGamesById(id: Int): Flow<DataClasses.GameWithPartialGames>

    @Query("SELECT * FROM Game WHERE rivalry = :rivalryId")
    fun getGamesFromRivalryIdAsList(rivalryId: Int): List<Game>

    @Query("SELECT * FROM Game WHERE id = :id")
    fun getGameById(id: Int): Game

    @Query("SELECT id, visitor_player, home_player FROM Rivalry WHERE id = :rivalryId")
    fun getRivalryPlayerIds(rivalryId: Int): DataClasses.RivalryPlayerIdTuple

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg games: Game)

    @Update
    suspend fun updateAll(vararg games: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("DELETE FROM Game")
    suspend fun deleteAll()


}