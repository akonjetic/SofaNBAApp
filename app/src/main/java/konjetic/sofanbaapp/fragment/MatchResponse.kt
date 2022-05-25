package konjetic.sofanbaapp.fragment

import konjetic.sofanbaapp.network.model.PlayersResponseMeta
import konjetic.sofanbaapp.network.model.TeamResponseData
import java.io.Serializable

data class MatchResponse(
    val data : ArrayList<MatchResponseData>,
    val meta : PlayersResponseMeta
) : Serializable

data class MatchResponseData(
    val id: Long,
    val date: String,
    val home_team: TeamResponseData,
    val home_team_score: Int,
    val period: Int,
    val postseason: Boolean,
    val season: Int,
    val status: String,
    val time: String,
    val visitor_team: TeamResponseData,
    val visitor_team_score: Int
)

