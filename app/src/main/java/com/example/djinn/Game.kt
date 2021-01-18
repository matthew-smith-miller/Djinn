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

    companion object GameManager {
        fun makeGame(number: Int, rivalry: Int): Game {
            return Game(
                0,
                number,
                rivalry
            )
        }
    }
}