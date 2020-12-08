package com.example.djinn

data class Rivalry(val id: Int, val homePlayer: Int, val visitorPlayer: Int) {
    var visitorScore: Int = 0
    var homeScore: Int = 0
    val games = arrayListOf<Game>()

    companion object RivalryManager{
        var rivalryCount = 0
        val rivalries = arrayListOf<Rivalry>()
        val rivalryMap = hashMapOf<Int, Rivalry>()

        fun makeRivalry(opponent: Int): Rivalry {
            return Rivalry(rivalryCount, HOME_PLAYER, opponent)
        }

        fun getRivalry(id: Int): Rivalry? {
            return rivalryMap[id]
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