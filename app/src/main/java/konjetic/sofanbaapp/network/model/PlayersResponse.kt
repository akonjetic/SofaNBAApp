package konjetic.sofanbaapp.network.model

import java.io.Serializable

data class PlayersResponse (
    val data: ArrayList<PlayersResponseData>,
    val meta: PlayersResponseMeta,
    //val results : ArrayList<PlayersResponse>
): Serializable



data class PlayersResponseData(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val position: String,
    val height_feet: Int,
    val height_inches: Int,
    val team: PlayersResponseDataTeam,
    val weight_pounds: Int
) : Serializable

data class PlayersResponseDataTeam(
    val id: Long,
    val abbreviation: String,
    val city: String,
    val conference: String,
    val division: String,
    val full_name: String,
    val name: String
): Serializable

data class PlayersResponseMeta(
    val total_pages: Int,
    val current_page: Int,
    val next_page: Int,
    val per_page: Int,
    val total_count: Int
): Serializable

data class PlayerImgResponse(
    val data : ArrayList<PlayerImgResponseData>
)

data class PlayerImgResponseData(
    val playerId: Long,
    val imageUrl: String,
    val imageCaption: String,
    val id: Long
)