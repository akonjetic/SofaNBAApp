package konjetic.sofanbaapp.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import konjetic.sofanbaapp.network.model.PlayersResponseData
import java.io.Serializable

@Entity(tableName = "FavoritePlayers")
data class PlayerData(
    @PrimaryKey
    var id: Long,
    var first_name: String,
    var last_name: String,
    var position: String,
    var height_feet: Int,
    var height_inches: Int,
    @Embedded var team: TeamInfo,
    var weight_pounds: Int,
    var positionDB: Int,
) : Serializable {
    constructor() : this(0, "", "", "", 0, 0, TeamInfo(0, "", "", "", "", "", ""), 0, 0)

    fun toResponseData(): PlayersResponseData {
        return PlayersResponseData(
            id, first_name, last_name, position, height_feet, height_inches, team.toTeamResponseData(), weight_pounds
        )
    }
}
