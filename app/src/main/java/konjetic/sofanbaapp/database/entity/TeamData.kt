package konjetic.sofanbaapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteTeams")
data class TeamData (
    @PrimaryKey
    var id:Long,
    var abbreviation: String,
    var city: String,
    var conference: String,
    var division: String,
    var full_name: String,
    var name: String,
    var position: Int
) {
    constructor() : this(0, "", "", "", "", "", "", 0)
}

/*@Entity(tableName = "FavoritePlayers")
data class PlayerData (
    @PrimaryKey
    var id: Long,
    var first_name: String,
    var last_name: String,
    var abbreviation: String,
    var position: Int
) {
    constructor() : this(0, "", "", "", 0)
}*/