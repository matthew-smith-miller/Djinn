package com.example.djinn

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.HashMap

class GameRepository(
    private val gameDao: GameDao,
    private val rivalryRepository: RivalryRepository,
    private val partialGameDao: PartialGameDao
) {
    fun getAllGames(): Flow<List<Game>> {
        return gameDao.getAllGames()
    }
    fun getGameWithPartialGamesById(id: Int): Flow<DataClasses.GameWithPartialGames> {
        return gameDao.getGameWithPartialGamesById(id)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun rollupScore(gameIds: List<Int>): List<Int> {
        val gamesWithPartialGames = gameDao.getGamesWithPartialGamesById(gameIds)
        val rivalryIdsToUpdate = gamesWithPartialGames.map { it.game.rivalry }
        val rivalryIdToPlayerIdsMap =
            gameDao.getRivalryPlayerIds(rivalryIdsToUpdate).map { it.id to it }.toMap()
        val gamesToUpdate = mutableListOf<Game>()
        val bonusesToInsert = mutableListOf<PartialGame>()
        for (gameWithPartialGames in gamesWithPartialGames) {
            val rivalryPlayerIdTuple = rivalryIdToPlayerIdsMap[gameWithPartialGames.game.rivalry]
            if (rivalryPlayerIdTuple != null) {
                var homePartialGames = 0
                var homeScore = 0
                var homeBonus = 0
                var visitorPartialGames = 0
                var visitorScore = 0
                var visitorBonus = 0
                for (partialGame in gameWithPartialGames.partialGames) {
                    when (partialGame.player) {
                        rivalryPlayerIdTuple.visitorPlayerId -> when (partialGame.type) {
                            "Bonus" -> visitorBonus += partialGame.totalScore
                            else -> {
                                visitorScore += partialGame.totalScore
                                visitorPartialGames++
                            }
                        }
                        else -> when (partialGame.type) {
                            "Bonus" -> homeBonus += partialGame.totalScore
                            else -> {
                                homeScore += partialGame.totalScore
                                homePartialGames++
                            }
                        }
                    }
                }
                if (gameWithPartialGames.game.status == "Completed") {
                    gameWithPartialGames.game.visitorScore = visitorScore + visitorBonus
                    gameWithPartialGames.game.homeScore = homeScore + homeBonus
                } else {
                    gameWithPartialGames.game.visitorScore = visitorScore
                    gameWithPartialGames.game.homeScore = homeScore
                    if (gameWithPartialGames.game.visitorScore >= 100 ||
                        gameWithPartialGames.game.homeScore >= 100
                    ) {
                        gameWithPartialGames.game.status = "Completed"
                        gameWithPartialGames.game.endDate = Calendar.getInstance().time
                        if (gameWithPartialGames.game.homeScore >= 100) {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.homePlayerId,
                                    "Win bonus",
                                    100
                                )
                            )
                            gameWithPartialGames.game.homeScore += 100
                        } else {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.visitorPlayerId,
                                    "Win bonus",
                                    100
                                )
                            )
                            gameWithPartialGames.game.visitorScore += 100
                        }
                        if (homePartialGames > 0) {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.homePlayerId,
                                    "Partial game bonus",
                                    homePartialGames * 20
                                )
                            )
                            gameWithPartialGames.game.homeScore += homePartialGames * 20
                        } else {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.visitorPlayerId,
                                    "Shutout bonus",
                                    100
                                )
                            )
                            gameWithPartialGames.game.visitorScore += 100
                        }
                        if (visitorPartialGames > 0) {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.visitorPlayerId,
                                    "Partial game bonus",
                                    visitorPartialGames * 20
                                )
                            )
                            gameWithPartialGames.game.visitorScore += visitorPartialGames * 20
                        } else {
                            bonusesToInsert.add(
                                PartialGame.makeBonus(
                                    gameWithPartialGames.game.id,
                                    rivalryPlayerIdTuple.homePlayerId,
                                    "Shutout bonus",
                                    100
                                )
                            )
                            gameWithPartialGames.game.homeScore += 100
                        }
                    }

                }
                gamesToUpdate.add(gameWithPartialGames.game)
            }
        }
        gameDao.update(gamesToUpdate)
        partialGameDao.insert(bonusesToInsert)
        return rivalryIdsToUpdate
    }

    /*@Suppress("RedundantSuspendModifier")
    suspend fun incrementScore(partialGame: PartialGame) {
        gameDao.getGameWithPartialGamesById(partialGame.game).collect { gameWithPartialGames ->
            gameDao.getRivalryPlayerIds(gameWithPartialGames.game.rivalry)
                .collect { rivalryPlayerIdTuple ->
                    when (partialGame.player) {
                        rivalryPlayerIdTuple.homePlayerId ->
                            gameWithPartialGames.game.homeScore += partialGame.totalScore
                        else ->
                            gameWithPartialGames.game.visitorScore += partialGame.totalScore
                    }
                    if ((gameWithPartialGames.game.homeScore >= 100 || gameWithPartialGames.game.visitorScore >= 100)
                        && gameWithPartialGames.game.status == "Active"
                    ) {
                        gameWithPartialGames.game.status = "Completed"
                        gameWithPartialGames.game.endDate = Calendar.getInstance().time
                        var homePartialBonus = 0
                        var visitorPartialBonus = 0

                        if (gameWithPartialGames.game.homeScore > 100) {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.homePlayerId,
                                "Win bonus",
                                100
                            )
                        } else if (gameWithPartialGames.game.visitorScore > 100) {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.visitorPlayerId,
                                "Win bonus",
                                100
                            )
                        }

                        for (partialGame in gameWithPartialGames.partialGames) {
                            if (partialGame.type != "Bonus") {
                                when (partialGame.player) {
                                    rivalryPlayerIdTuple.homePlayerId -> homePartialBonus += 20
                                    else -> visitorPartialBonus += 20
                                }
                            }
                        }
                        if (homePartialBonus > 0) {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.homePlayerId,
                                "Partial game bonus",
                                homePartialBonus
                            )
                        } else {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.visitorPlayerId,
                                "Shutout bonus",
                                100
                            )
                        }
                        if (visitorPartialBonus > 0) {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.visitorPlayerId,
                                "Partial game bonus",
                                visitorPartialBonus
                            )
                        } else {
                            PartialGame.makeBonus(
                                gameWithPartialGames.game.id,
                                rivalryPlayerIdTuple.homePlayerId,
                                "Shutout bonus",
                                100
                            )
                        }
                        rivalryRepository.incrementScore(gameWithPartialGames.game)
                    }
                    gameDao.update(gameWithPartialGames.game)
                }

        }
    }*/

    @Suppress("RedundantSuspendModifier")
    suspend fun insert(game: Game): Long {
        return gameDao.insert(game)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun update(game: Game) {
        gameDao.update(game)
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun update(games: List<Game>) {
        gameDao.update(games)
    }
}