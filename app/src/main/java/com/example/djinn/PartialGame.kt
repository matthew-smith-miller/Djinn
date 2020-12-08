package com.example.djinn

data class PartialGame(
    val id: Int,
    val game: Int,
    val player: Int,
    val type: String = "K",
    val note: String?,
    val rawScore: Int = 0
) {

    val totalScore = rawScore + when (type) {
        "G" -> 20
        "R" -> 10
        else -> 0
    }

    companion object PartialGameManager {
        var partialGameCount = 0
        val partialGameMap = hashMapOf<Int, PartialGame>()

        fun makePartialGame(
            game: Int,
            player: Int,
            type: String,
            rawScore: Int
        ): PartialGame {
            return PartialGame(partialGameCount, game, player, type, null, rawScore)
        }

        fun makeBonus(
            game: Int,
            player: Int,
            note: String,
            rawScore: Int
        ): PartialGame {
            return PartialGame(partialGameCount, game, player, "B", note, rawScore)
        }

        fun getPartialGame(id: Int?): PartialGame? {
            return when (id) {
                null -> null
                else -> partialGameMap[id]
            }
        }
    }

    init {
        partialGameCount++
        Game.getGame(game)?.partialGames?.add(this)
        partialGameMap[id] = this
        Game.getGame(game)?.incrementScore(totalScore, player)
    }
}