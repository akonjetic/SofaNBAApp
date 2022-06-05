package konjetic.sofanbaapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import konjetic.sofanbaapp.network.model.TeamResponseData
import java.io.Serializable

@Entity(tableName = "FavoriteTeams")
data class TeamData(
    @PrimaryKey
    var id: Long,
    var abbreviation: String,
    var city: String,
    var conference: String,
    var division: String,
    var full_name: String,
    var name: String,
    var position: Int
) : Serializable {
    constructor() : this(0, "", "", "", "", "", "", 0)

    fun mapToTeamInfo(): TeamInfo {
        return TeamInfo(
            id, abbreviation, city, conference, division, full_name, name
        )
    }

    fun mapToTeamResponseData(): TeamResponseData {
        return TeamResponseData(
            id, abbreviation, city, conference, division, full_name, name
        )
    }
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
