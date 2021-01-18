package com.example.djinn

import androidx.room.ColumnInfo
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

    data class IdNameTuple(
        val id: Int,
        val name: String
    )

    data class RivalryPlayerIdTuple(
        val id: Int,
        @ColumnInfo(name = "visitor_player") val visitorPlayerId: Int,
        @ColumnInfo(name = "home_player") val homePlayerId: Int
    )

}