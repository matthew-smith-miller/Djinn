package com.example.djinn

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Rivalry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "visitor_player") val visitorPlayer: Int,
    @ColumnInfo(name = "home_player") val homePlayer: Int,
    @ColumnInfo(name = "visitor_score") var visitorScore: Int = 0,
    @ColumnInfo(name = "home_score") var homeScore: Int = 0,
    @ColumnInfo(name = "created_date") val createdDate: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "last_modified_date") var lastModifiedDate: Date = createdDate
) {
    @Ignore
    val games = arrayListOf<Game>()

    companion object RivalryManager {
        fun makeRivalry(visitorPlayer: Int, homePlayer: Int): Rivalry {
            return Rivalry(0, visitorPlayer, homePlayer)
        }
    }
}