package konjetic.sofanbaapp.network.model

import konjetic.sofanbaapp.fragment.MatchResponseData
import java.io.Serializable

data class StatsResponse(
    val data: ArrayList<StatsResponseData>,
    val meta: PlayersResponseMeta
) : Serializable

data class StatsResponseData(
    val id: Long,
    val ast: Int,
    val blk: Int,
    val dreb: Int,
    val fg3_pct: Double,
    val fg3a: Int,
    val fg3m: Int,
    val fg_pct: Double,
    val fga: Int,
    val fgm: Int,
    val ft_pct: Double,
    val fta: Int,
    val ftm: Int,
    val game: StatsResponseDataGame,
    val min: String,
    val oreb: Int,
    val pf: Int,
    val player: StatsResponseDataPlayer,
    val pts: Int,
    val reb: Int,
    val stl: Int,
    val team: TeamResponseData,
    val turnover: Int
) : Serializable

data class StatsResponseDataPlayer(
    val id: Long,
    val first_name: String,
    val height_feet: Int,
    val height_inches: Int,
    val last_name: String,
    val position: String,
    val team_id: Long,
    val weight_pounds: Int
) : Serializable

data class StatsResponseDataGame(
    val id: Long,
    val date: String,
    val home_team_id: Long,
    val home_team_score: Int,
    val period: Int,
    val postseason: Boolean,
    val season: Int,
    val status: String,
    val time: String,
    val visitor_team_id: Long,
    val visitor_team_score: Int
) : Serializable {

    fun toMatchResponseData(home: TeamResponseData, visitor: TeamResponseData): MatchResponseData {
        return MatchResponseData(
            id,
            date,
            home,
            home_team_score,
            period,
            postseason,
            season,
            status,
            time,
            visitor,
            visitor_team_score
        )
    }
}
