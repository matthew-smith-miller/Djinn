package com.example.djinn

import android.app.Application

class DjinnApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val playerRepository by lazy { PlayerRepository(database.playerDao()) }
    val rivalryRepository by lazy { RivalryRepository(database.rivalryDao()) }
    val gameRepository by lazy { GameRepository(database.gameDao()) }
    val partialGameRepository by lazy { PartialGameRepository(database.partialGameDao()) }
}