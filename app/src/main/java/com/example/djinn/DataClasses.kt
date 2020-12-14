package com.example.djinn

import androidx.room.Embedded
import androidx.room.Relation

class DataClasses {

    data class RivalryWithGames(
        @Embedded val rivalry: Rivalry,
        @Relation(
            parentColumn = "id",
            entityColumn = "rivalry"
        )
        val games: List<Game>
    )

    data class GameWithPartialGames(
        @Embedded val game: Game,
        @Relation(
            parentColumn = "id",
            entityColumn = "game"
        )
        val partialGames: List<PartialGame>
    )

}