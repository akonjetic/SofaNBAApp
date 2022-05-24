package konjetic.sofanbaapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoritePlayers")
data class PlayerData (
    @PrimaryKey
    var id: Long,
    var first_name: String,
    var last_name: String,
    var abbreviation: String,
    var position: Int,
    var teamName: String
) {
    constructor() : this(0, "", "", "", 0, "")
}