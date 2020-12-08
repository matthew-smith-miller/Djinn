package com.example.djinn

import java.time.LocalDateTime

data class Player(val id: Int, var name: String, var initials: String) {
    val createdDate = LocalDateTime.now()
    var lastModifiedDate = createdDate

    companion object PlayerManager {
        var playerCount = 0
        val players = arrayListOf<Player>()
        val playerMap = hashMapOf<Int,Player>()

        fun makePlayer(name: String): Player {
            val names = name.split(" ")
            return Player(
                playerCount,
                name,
                when (names.size) {
                    3 -> names[0].substring(0,1).plus(names[2].substring(0,1))
                    2 -> names[0].substring(0,1).plus(names[1].substring(0,1))
                    1 -> names[0].substring(0,1)
                    0 -> "N/A"
                    else -> names[0].substring(0,1).plus(names.last().substring(0,1))
                }
            )
        }

        fun getPlayer(id: Int): Player? {
             return playerMap[id]
        }
    }

    init {
        playerCount++
        playerMap[id] = this
        players.add(this)
    }
}