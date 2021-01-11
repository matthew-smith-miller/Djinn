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

            matt = playerDao.getPlayerFromName("Matt Miller")
            neil = playerDao.getPlayerFromName("Neil Katuna")
            scott = playerDao.getPlayerFromName("Scott Liftman")
            barack = playerDao.getPlayerFromName("Barack Obama")

            val neilRivalry = Rivalry.makeRivalry(neil.id, matt.id)
            var scottRivalry = Rivalry.makeRivalry(scott.id, matt.id)
            val barackRivalry = Rivalry.makeRivalry(barack.id, matt.id)
            rivalryDao.insertAll(neilRivalry, scottRivalry, barackRivalry)

            scottRivalry = rivalryDao.getRivalryFromPlayerIds(scott.id, matt.id)

            for (Int in 1..8) {
                gameDao.insertAll(Game.makeGame(scottRivalry.id))
            }

            val scottGames = gameDao.getGamesFromRivalryIdAsList(scottRivalry.id)

            partialGameDao.insertAll(
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 6),
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 39),
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 29),
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 8),
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 9),
                PartialGame.makePartialGame(scottGames[0].id, matt.id, "Knock", 62),
                PartialGame.makePartialGame(scottGames[0].id, matt.id, "Knock", 10),
                PartialGame.makePartialGame(scottGames[0].id, matt.id, "Knock", 22),
                PartialGame.makePartialGame(scottGames[0].id, matt.id, "Knock", 5),
                PartialGame.makePartialGame(scottGames[0].id, scott.id, "Knock", 28),
                PartialGame.makePartialGame(scottGames[1].id, matt.id, "Knock", 57),
                PartialGame.makePartialGame(scottGames[1].id, matt.id, "Knock", 24),
                PartialGame.makePartialGame(scottGames[1].id, matt.id, "Knock", 9),
                PartialGame.makePartialGame(scottGames[1].id, matt.id, "Knock", 33),
                PartialGame.makePartialGame(scottGames[2].id, matt.id, "Gin", 60),
                PartialGame.makePartialGame(scottGames[2].id, scott.id, "Knock", 44),
                PartialGame.makePartialGame(scottGames[2].id, matt.id, "Knock", 27),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Undercut", 2),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Knock", 40),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Knock", 13),
                PartialGame.makePartialGame(scottGames[3].id, scott.id, "Knock", 5),
                PartialGame.makePartialGame(scottGames[3].id, scott.id, "Gin", 24),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Knock", 13),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Knock", 20),
                PartialGame.makePartialGame(scottGames[3].id, scott.id, "Knock", 12),
                PartialGame.makePartialGame(scottGames[3].id, scott.id, "Knock", 28),
                PartialGame.makePartialGame(scottGames[3].id, matt.id, "Knock", 14),
                PartialGame.makePartialGame(scottGames[4].id, scott.id, "Knock", 24),
                PartialGame.makePartialGame(scottGames[4].id, matt.id, "Knock", 14),
                PartialGame.makePartialGame(scottGames[4].id, matt.id, "Knock", 11),
                PartialGame.makePartialGame(scottGames[4].id, matt.id, "Knock", 58),
                PartialGame.makePartialGame(scottGames[4].id, matt.id, "Knock", 41),
                PartialGame.makePartialGame(scottGames[5].id, scott.id, "Knock", 30),
                PartialGame.makePartialGame(scottGames[5].id, scott.id, "Knock", 41),
                PartialGame.makePartialGame(scottGames[5].id, matt.id, "Knock", 17),
                PartialGame.makePartialGame(scottGames[5].id, scott.id, "Knock", 19),
                PartialGame.makePartialGame(scottGames[5].id, scott.id, "Knock", 12),
                PartialGame.makePartialGame(scottGames[6].id, matt.id, "Knock", 34),
                PartialGame.makePartialGame(scottGames[6].id, scott.id, "Knock", 17),
                PartialGame.makePartialGame(scottGames[6].id, scott.id, "Undercut", 0),
                PartialGame.makePartialGame(scottGames[6].id, matt.id, "Gin", 40),
                PartialGame.makePartialGame(scottGames[6].id, scott.id, "Knock", 7),
                PartialGame.makePartialGame(scottGames[6].id, matt.id, "Knock", 42),
                PartialGame.makePartialGame(scottGames[7].id, scott.id, "Knock", 18),
                PartialGame.makePartialGame(scottGames[7].id, scott.id, "Knock", 23),
                PartialGame.makePartialGame(scottGames[7].id, scott.id, "Undercut", 3),
                PartialGame.makePartialGame(scottGames[7].id, scott.id, "Knock", 45),
                PartialGame.makePartialGame(scottGames[7].id, scott.id, "Knock", 10)
            )
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
