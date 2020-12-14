package com.example.djinn

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var initials: String,
    @ColumnInfo(name = "created_date") val createdDate: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "last_modified_date") var lastModifiedDate: Date = createdDate
) {

    companion object PlayerManager {
        var playerCount = 0
        val players = arrayListOf<Player>()
        val playerMap = hashMapOf<Int, Player>()

        fun makePlayer(name: String): Player {
            val names = name.split(" ")
            return Player(
                playerCount,
                name,
                when (names.size) {
                    3 -> names[0].substring(0, 1).plus(names[2].substring(0, 1))
                    2 -> names[0].substring(0, 1).plus(names[1].substring(0, 1))
                    1 -> names[0].substring(0, 1)
                    0 -> "N/A"
                    else -> names[0].substring(0, 1).plus(names.last().substring(0, 1))
                }
            )
        }

        fun getPlayer(id: Int?): Player? {
            return when (id) {
                null -> null
                else -> playerMap[id]
            }
        }
    }

    init {
        playerCount++
        playerMap[id] = this
        players.add(this)
    }
}