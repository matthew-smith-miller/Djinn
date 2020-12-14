package com.example.djinn

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = arrayOf(
        Player::class,
        Rivalry::class,
        Game::class,
        PartialGame::class
    ),
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): Daos.PlayerDao
    abstract fun rivalryDao(): Daos.RivalryDao
    abstract fun gameDao(): Daos.GameDao
    abstract fun partialGameDao(): Daos.PartialGameDao
}
