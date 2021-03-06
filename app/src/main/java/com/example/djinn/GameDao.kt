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

    @Query("SELECT * FROM Game WHERE id IN (:ids)")
    fun getGameWithPartialGamesById(ids: List<Int>): Flow<List<DataClasses.GameWithPartialGames>>

    @Query("SELECT * FROM Game WHERE id IN (:ids)")
    suspend fun getGamesWithPartialGamesById(ids: List<Int>): List<DataClasses.GameWithPartialGames>

    @Query("SELECT * FROM Game WHERE rivalry = :rivalryId")
    fun getGamesFromRivalryIdAsList(rivalryId: Int): List<Game>

    @Query("SELECT * FROM Game WHERE id = :id")
    fun getGameById(id: Int): Game

    @Query("SELECT id, visitor_player, home_player FROM Rivalry WHERE id IN (:rivalryIds)")
    suspend fun getRivalryPlayerIds(rivalryIds: List<Int>): List<DataClasses.RivalryPlayerIdTuple>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg games: Game)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game): Long

    @Update
    suspend fun update(vararg games: Game)

    @Update
    suspend fun update(games: List<Game>)

    @Delete
    suspend fun delete(game: Game)

    @Query("DELETE FROM Game")
    suspend fun deleteAll()


}