package com.example.djinn

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RivalryDao {
    @Query("SELECT * FROM Rivalry")
    fun getAllRivalries(): Flow<List<Rivalry>>

    @Transaction
    @Query("SELECT * FROM Rivalry")
    fun getAllRivalriesWithGames(): Flow<List<DataClasses.RivalryWithGames>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg rivalries: Rivalry)

    @Delete
    suspend fun delete(rivalry: Rivalry)
}