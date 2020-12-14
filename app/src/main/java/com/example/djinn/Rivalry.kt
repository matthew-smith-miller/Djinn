package com.example.djinn

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Rivalry(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "home_player") val homePlayer: Int,
    @ColumnInfo(name = "visitor_player") val visitorPlayer: Int,
    @ColumnInfo(name = "visitor_score") var visitorScore: Int = 0,
    @ColumnInfo(name = "home_score") var homeScore: Int = 0,
    @ColumnInfo(name = "created_date") val createdDate: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "last_modified_date") var lastModifiedDate: Date = createdDate
) {
    @Ignore
    val games = arrayListOf<Game>()

    companion object RivalryManager {
        var rivalryCount = 0
        val rivalries = arrayListOf<Rivalry>()
        val rivalryMap = hashMapOf<Int, Rivalry>()

        fun makeRivalry(opponent: Int): Rivalry {
            return Rivalry(rivalryCount, HOME_PLAYER, opponent)
        }

        fun getRivalry(id: Int?): Rivalry? {
            return when (id) {
                null -> null
                else -> rivalryMap[id]
            }
        }
    }

    init {
        rivalryCount++
        rivalries.add(this)
        rivalryMap[id] = this
    }

    fun incrementScore(visitorIncrement: Int, homeIncrement: Int) {
        visitorScore += visitorIncrement
        homeScore += homeIncrement
    }
}