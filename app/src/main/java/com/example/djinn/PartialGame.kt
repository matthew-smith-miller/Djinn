package com.example.djinn

data class PartialGame (
    val id: Int,
    val game: Int,
    val player: Int,
    val type: String = "K",
    val rawScore: Int = 0) {

    val totalScore = rawScore + when (type) {
        "G" -> 20
        "R" -> 10
        else -> 0
    }

    companion object PartialGameManager{
        var partialGameCount = 0
        val partialGameMap = hashMapOf<Int, PartialGame>()

        fun makePartialGame(game: Int, player: Int, type: String, rawScore: Int): PartialGame {
            return PartialGame(partialGameCount, game, player, type, rawScore)
        }

        fun getPartialGame(id: Int): PartialGame? {
            return partialGameMap[id]
        }
    }

    init {
        partialGameCount++
        Game.getGame(game)?.partialGames?.add(this)
        partialGameMap[id] = this
        Game.getGame(game)?.incrementScore(totalScore, player)
    }
}