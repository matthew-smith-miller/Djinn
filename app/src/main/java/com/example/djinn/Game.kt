package com.example.djinn
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val number: Int,
    val rivalry: Int,
    @ColumnInfo(name = "home_score") var homeScore: Int = 0,
    @ColumnInfo(name = "visitor_score") var visitorScore: Int = 0,
    var status: String = "Active",
    @ColumnInfo(name = "end_date") var endDate: Date? = null
) {


    @Ignore
    val partialGames = arrayListOf<PartialGame>()

    companion object GameManager {
        val gameMap = hashMapOf<Int, Game>()

        fun makeGame(rivalry: Int): Game {
            val numberOfGames = Rivalry.getRivalry(rivalry)?.games?.size
            return Game(
                0,
                if (numberOfGames != null) {
                    numberOfGames + 1 ?: 0
                } else {
                    0
                },
                rivalry
            )
        }

        fun getGame(id: Int?): Game? {
            return when (id) {
                null -> null
                else -> gameMap[id]
            }
        }
    }

    init {
        Rivalry.getRivalry(rivalry)?.games?.add(this)
        gameMap[id] = this
    }

    fun incrementScore(totalScore: Int, player: Int) {
        when (player) {
            Rivalry.getRivalry(rivalry)?.homePlayer ->
                homeScore += totalScore
            Rivalry.getRivalry(rivalry)?.visitorPlayer ->
                visitorScore += totalScore
        }
        if ((homeScore >= 100 || visitorScore >= 100) && status == "Active") {
            endGame();
        }
    }

    private fun endGame() {
        status = "Completed"
        endDate = Calendar.getInstance().time
        var homePartialBonus = 0
        var visitorPartialBonus = 0

        if (homeScore > 100) {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Win bonus", 100
                ) }
        } else if (visitorScore > 100) {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Win bonus", 100
                ) }
        }

        for (partialGame in partialGames) {
            if (partialGame.type != "Bonus") {
                when (partialGame.player) {
                    Rivalry.getRivalry(rivalry)?.homePlayer -> homePartialBonus += 20
                    Rivalry.getRivalry(rivalry)?.visitorPlayer -> visitorPartialBonus += 20
                }
            }
        }
        if (homePartialBonus > 0) {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Partial game bonus", homePartialBonus
                ) }
        } else {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Shutout bonus", 100
                ) }
        }
        if (visitorPartialBonus > 0) {
            Rivalry.getRivalry(rivalry)?.visitorPlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Partial game bonus", visitorPartialBonus
                ) }
        } else {
            Rivalry.getRivalry(rivalry)?.homePlayer?.let {
                PartialGame.makeBonus(
                    id, it, "Shutout bonus", 100
                ) }
        }
        Rivalry.getRivalry(rivalry)?.incrementScore(visitorScore, homeScore)
    }
}