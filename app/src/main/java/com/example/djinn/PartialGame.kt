package com.example.djinn

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PartialGame(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val game: Int,
    val player: Int,
    val type: String,
    val note: String?,
    @ColumnInfo(name = "raw_score") val rawScore: Int,
    @ColumnInfo(name = "total_score") val totalScore: Int = rawScore + when (type) {
        "Gin" -> R.integer.bonus_gin
        "Undercut" -> R.integer.bonus_undercut
        else -> R.integer.bonus_knock
    }
) {

    companion object PartialGameManager {
        fun makePartialGame(
            game: Int,
            player: Int,
            type: String,
            rawScore: Int
        ): PartialGame {
            return PartialGame(
                0,
                game,
                player,
                type,
                when (type) {
                    "Bonus" -> null
                    else -> type
                },
                rawScore
            )
        }

        fun makeBonus(
            game: Int,
            player: Int,
            note: String,
            rawScore: Int
        ): PartialGame {
            return PartialGame(
                0,
                game,
                player,
                "Bonus",
                note,
                rawScore
            )
        }
    }
}