package com.example.djinn

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DjinnApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val playerRepository by lazy { PlayerRepository(database.playerDao()) }
    val rivalryRepository by lazy { RivalryRepository(database.rivalryDao()) }
    val gameRepository by lazy { GameRepository(database.gameDao()) }
    val partialGameRepository by lazy { PartialGameRepository(database.partialGameDao()) }
}