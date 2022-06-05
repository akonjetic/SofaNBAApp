package konjetic.sofanbaapp.network.model

import konjetic.sofanbaapp.database.entity.PlayerData
import java.io.Serializable

data class PlayersResponse(
    val data: ArrayList<PlayersResponseData>,
    val meta: PlayersResponseMeta
) : Serializable

data class PlayersResponseData(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val position: String,
    val height_feet: Int,
    val height_inches: Int,
    val team: TeamResponseData,
    val weight_pounds: Int
) : Serializable {

    fun toPlayerData(position: Int): PlayerData {
        return PlayerData(
            id, first_name, last_name, this.position, height_feet, height_inches, team.toTeamInfo(), weight_pounds, position
        )
    }
}

data class PlayersResponseMeta(
    val total_pages: Int,
    val current_page: Int,
    val next_page: Int,
    val per_page: Int,
    val total_count: Int
) : Serializable

data class PlayerImgResponse(
    val data: ArrayList<PlayerImgResponseData>
)

data class PlayerImgResponseData(
    val playerId: Long,
    val imageUrl: String,
    val imageCaption: String,
    val id: Long
)

data class PlayerImgPost(
    val playerId: Long,
    val imageUrl: String,
    val imageCaption: String
) : Serializable
