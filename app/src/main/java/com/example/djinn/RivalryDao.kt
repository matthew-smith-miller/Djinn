package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RivalryDao {
    @Query("SELECT * FROM Rivalry")
    fun getAllRivalries(): Flow<List<Rivalry>>

    @Query("SELECT * FROM Rivalry WHERE visitor_player = :visitorPlayer AND home_player = :homePlayer")
    fun getRivalryFromPlayerIds(visitorPlayer: Int, homePlayer: Int): Rivalry

    @Query("SELECT * FROM Rivalry WHERE id = :id")
    fun getRivalryById(id: Int): Flow<Rivalry>

    @Transaction
    @Query("SELECT * FROM Rivalry WHERE id = :id")
    fun getRivalryWithGamesById(id: Int): Flow<DataClasses.RivalryWithGames>

    @Transaction
    @Query("SELECT * FROM Rivalry WHERE id IN (:ids)")
    fun getRivalryWithGamesById(ids: Set<Int>): Flow<List<DataClasses.RivalryWithGames>>

    @Transaction
    @Query("SELECT * FROM Rivalry")
    fun getAllRivalriesWithGames(): Flow<List<DataClasses.RivalryWithGames>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg rivalries: Rivalry)

    @Update
    suspend fun update(vararg rivalries: Rivalry)

    @Update
    suspend fun update(rivalries: List<Rivalry>)

    @Delete
    suspend fun delete(rivalry: Rivalry)

    @Query("DELETE FROM Rivalry")
    suspend fun deleteAll()
}