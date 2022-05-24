package konjetic.sofanbaapp.network.model

import java.io.Serializable

data class TeamResponse (
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
) : Serializable

