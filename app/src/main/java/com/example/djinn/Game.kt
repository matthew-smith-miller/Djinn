package com.example.djinn
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Game (val id: Int, val rivalry: Int) {
    val number: Int = Rivalry.getRivalry(rivalry)?.games?.size ?: id
    var homeScore: Int = 0
    var visitorScore: Int = 0
    val partialGames = arrayListOf<PartialGame>()
    var status = "Active"
    var endDate : LocalDate? = null

    companion object GameManager{
        var gameCount = 0
        val gameMap = hashMapOf<Int, Game>()

        fun makeGame(rivalry: Int): Game {
            return Game(gameCount, rivalry)
        }

        fun getGame(id: Int): Game? {
            return gameMap[id]
        }
    }

    init {
        gameCount++
        Rivalry.getRivalry(rivalry)?.games?.add(this)
        gameMap[id] = this
    }

    fun incrementScore(totalScore: Int, player: Int) {
        when (player) {
            Rivalry.getRivalry(rivalry)?.homePlayer ->
                homeScore += totalScore
            Rivalry.getRivalry(rivalry)?.visitorPlayer ->
                visitorScore += totalScore
        }
        if ((homeScore >= 100 || visitorScore >= 100) && status == "Active") {
            endGame();
        }
    }

    private fun endGame() {
        status = "Completed"
        endDate = LocalDate.now()
        var homePartialBonus = 0
        var visitorPartialBonus = 0

        if (homeScore > 100) {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Win bonus", 100) }
        } else if (visitorScore > 100) {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Win bonus", 100) }
        }

        for (partialGame in partialGames) {
            when (partialGame.player) {
                Rivalry.getRivalry(rivalry)?.homePlayer -> homePartialBonus += 20
                Rivalry.getRivalry(rivalry)?.visitorPlayer -> visitorPartialBonus += 20
            }
        }
        if (homePartialBonus > 0) {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Partial game bonus", homePartialBonus) }
        } else {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Shutout bonus", 100) }
        }
        if (visitorPartialBonus > 0) {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Partial game bonus", visitorPartialBonus) }
        } else {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makePartialGame(
                    id, it, "Shutout bonus", 100) }
        }
        Rivalry.getRivalry(rivalry)?.incrementScore(visitorScore, homeScore)
    }
}