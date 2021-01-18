package com.example.djinn

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Player::class, Rivalry::class, Game::class, PartialGame::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun rivalryDao(): RivalryDao
    abstract fun gameDao(): GameDao
    abstract fun partialGameDao(): PartialGameDao

    private class AppDatabaseCallBack(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.playerDao(),
                        database.rivalryDao(),
                        database.gameDao(),
                        database.partialGameDao()
                    )
                }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            /*INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.playerDao(),
                        database.rivalryDao(),
                        database.gameDao(),
                        database.partialGameDao()
                    )
                }
            }*/
        }

        suspend fun populateDatabase(
            playerDao: PlayerDao,
            rivalryDao: RivalryDao,
            gameDao: GameDao,
            partialGameDao: PartialGameDao
        ) {
            playerDao.deleteAll()
            rivalryDao.deleteAll()
            gameDao.deleteAll()
            partialGameDao.deleteAll()

            var matt = Player.makePlayer("Matt Miller", R.drawable.matt, true)
            var neil = Player.makePlayer("Neil Katuna", R.drawable.neil, false)
            var scott = Player.makePlayer("Scott Liftman", R.drawable.scott, false)
            var barack = Player.makePlayer("Barack Obama", R.drawable.barack, false)
            playerDao.insertAll(matt, neil, scott, barack)

            matt = playerDao.getPlayerByNameAsPlayer("Matt Miller")
            neil = playerDao.getPlayerByNameAsPlayer("Neil Katuna")
            scott = playerDao.getPlayerByNameAsPlayer("Scott Liftman")
            barack = playerDao.getPlayerByNameAsPlayer("Barack Obama")

            val neilRivalry = Rivalry.makeRivalry(neil.id, matt.id)
            var scottRivalry = Rivalry.makeRivalry(scott.id, matt.id)
            val barackRivalry = Rivalry.makeRivalry(barack.id, matt.id)
            rivalryDao.insertAll(neilRivalry, scottRivalry, barackRivalry)

            scottRivalry = rivalryDao.getRivalryFromPlayerIds(scott.id, matt.id)

            for (gameNumber in 1..8) {
                gameDao.insertAll(Game.makeGame(gameNumber, scottRivalry.id))
            }

            val scottGames = gameDao.getGamesFromRivalryIdAsList(scottRivalry.id)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
