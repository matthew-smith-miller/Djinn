package com.example.djinn

import androidx.room.*

class Daos {
    @Dao
    interface PlayerDao {
        @Query("SELECT * FROM Player")
        suspend fun getAll(): List<Player>

        @Query("SELECT * FROM Player WHERE id IN (:playerIds)")
        suspend fun loadAllByIds(playerIds: IntArray): List<Player>

        @Query("SELECT * FROM Player WHERE name LIKE :name LIMIT 1")
        suspend fun findByName(name: String): Player

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(vararg players: Player)

        @Delete
        suspend fun delete(player: Player)
    }

    @Dao
    interface RivalryDao {
        @Query("SELECT * FROM Rivalry")
        suspend fun getAll(): List<Rivalry>

        @Transaction
        @Query("SELECT * FROM Rivalry")
        suspend fun getRivalriesWithGames(): List<DataClasses.RivalryWithGames>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(vararg rivalries: Rivalry)

        @Delete
        suspend fun delete(rivalry: Rivalry)
    }

    @Dao
    interface GameDao {
        @Query("SELECT * FROM Game")
        suspend fun getAll(): List<Game>

        @Transaction
        @Query("SELECT * FROM Game")
        suspend fun getGamesWithPartialGames(): List<DataClasses.GameWithPartialGames>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(vararg games: Game)

        @Delete
        suspend fun delete(game: Game)
    }

    @Dao
    interface PartialGameDao {
        @Query("SELECT * FROM PartialGame")
        suspend fun getAll(): List<PartialGame>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(vararg partialGames: PartialGame)

        @Delete
        suspend fun delete(partialGame: PartialGame)
    }


}