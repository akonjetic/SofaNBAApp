package konjetic.sofanbaapp.network.model

import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.database.entity.TeamInfo
import java.io.Serializable

data class TeamResponse(
    val data: ArrayList<TeamResponseData>,
    val meta: PlayersResponseMeta
) : Serializable

data class TeamResponseData(
    val id: Long,
    val abbreviation: String,
    val city: String,
    val conference: String,
    val division: String,
    val full_name: String,
    val name: String
) : Serializable {
    fun toTeamInfo(): TeamInfo {
        return TeamInfo(
            id, abbreviation, city, conference, division, full_name, name
        )
    }

    fun toTeamData(position: Int): TeamData {
        return TeamData(
            id, abbreviation, city, conference, division, full_name, name, position
        )
    }
}
