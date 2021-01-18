package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.util.*

class GameRepository(
    private val gameDao: GameDao,
    private val rivalryRepository: RivalryRepository
) {

    fun getAllGames(): Flow<List<Game>> {
        return gameDao.getAllGames()
    }

    fun getGameWithPartialGamesById(id: Int): Flow<DataClasses.GameWithPartialGames> {
        return gameDao.getGameWithPartialGamesById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun incrementScore(partialGame: PartialGame) {
        var gameWithPartialGames: DataClasses.GameWithPartialGames? = null

        gameDao.getGameWithPartialGamesById(partialGame.game).collect {
            gameWithPartialGames = it
        }
        if (gameWithPartialGames != null) {
            val rivalryPlayerIdTuple =
                gameDao.getRivalryPlayerIds(gameWithPartialGames!!.game.rivalry)

            when (partialGame.player) {
                rivalryPlayerIdTuple.homePlayerId ->
                    gameWithPartialGames!!.game.homeScore += partialGame.totalScore
                else ->
                    gameWithPartialGames!!.game.visitorScore += partialGame.totalScore
            }
            if ((gameWithPartialGames!!.game.homeScore >= 100 || gameWithPartialGames!!.game.visitorScore >= 100)
                && gameWithPartialGames!!.game.status == "Active"
            ) {
                gameWithPartialGames!!.game.status = "Completed"
                gameWithPartialGames!!.game.endDate = Calendar.getInstance().time
                var homePartialBonus = 0
                var visitorPartialBonus = 0

                if (gameWithPartialGames!!.game.homeScore > 100) {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.homePlayerId,
                        "Win bonus",
                        100
                    )
                } else if (gameWithPartialGames!!.game.visitorScore > 100) {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.visitorPlayerId,
                        "Win bonus",
                        100
                    )
                }

                for (partialGame in gameWithPartialGames!!.partialGames) {
                    if (partialGame.type != "Bonus") {
                        when (partialGame.player) {
                            rivalryPlayerIdTuple.homePlayerId -> homePartialBonus += 20
                            else -> visitorPartialBonus += 20
                        }
                    }
                }
                if (homePartialBonus > 0) {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.homePlayerId,
                        "Partial game bonus",
                        homePartialBonus
                    )
                } else {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.visitorPlayerId,
                        "Shutout bonus",
                        100
                    )
                }
                if (visitorPartialBonus > 0) {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.visitorPlayerId,
                        "Partial game bonus",
                        visitorPartialBonus
                    )
                } else {
                    PartialGame.makeBonus(
                        gameWithPartialGames!!.game.id,
                        rivalryPlayerIdTuple.homePlayerId,
                        "Shutout bonus",
                        100
                    )
                }

                rivalryRepository.incrementScore(gameWithPartialGames!!.game)
            }
            gameDao.updateAll(gameWithPartialGames!!.game)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(game: Game) {
        gameDao.insertAll(game)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(game: Game) {
        gameDao.updateAll(game)
    }
}