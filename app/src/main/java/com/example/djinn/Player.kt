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
    var image: Int,
    @ColumnInfo(name = "image_name") var imageName: String?,
    @ColumnInfo(name = "is_home_player") val isHomePlayer: Boolean,
    @ColumnInfo(name = "created_date") val createdDate: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "last_modified_date") var lastModifiedDate: Date = createdDate
) {

    companion object PlayerManager {
        fun makePlayer(name: String, imageName: String?): Player {
            val names = name.split(" ")
            return Player(
                0,
                name,
                when (names.size) {
                    3 -> names[0].substring(0, 1).plus(names[2].substring(0, 1))
                    2 -> names[0].substring(0, 1).plus(names[1].substring(0, 1))
                    1 -> names[0].substring(0, 1)
                    0 -> "N/A"
                    else -> names[0].substring(0, 1).plus(names.last().substring(0, 1))
                },
                0,
                imageName,
                false
            )
        }
    }
}