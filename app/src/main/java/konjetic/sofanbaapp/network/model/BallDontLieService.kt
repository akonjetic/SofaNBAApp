package konjetic.sofanbaapp.network.model

import konjetic.sofanbaapp.fragment.MatchResponse
import konjetic.sofanbaapp.fragment.MatchResponseData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BallDontLieService {

    @GET("api/v1/players")
    suspend fun playerSearch(@Query("search") search: String): PlayersResponse

    @GET("api/v1/players")
    suspend fun getAllPlayers(@Query("per_page") per_page: Int, @Query("page") page: Int): PlayersResponse

    @GET("api/v1/players/{playerId}")
    suspend fun getSpecificPlayer(@Path("playerId") playerId: Long): PlayersResponseData

    @GET("api/v1/teams")
    suspend fun getAllTeams(@Query("per_page") per_page: Int, @Query("page") page: Int): TeamResponse

    @GET("api/v1/games")
    suspend fun getAllGames(@Query("per_page") per_page: Int, @Query("page") page: Int): MatchResponse

    @GET("api/v1/games/{gameId}")
    suspend fun getSpecificGame(@Path("gameId") gameId: Long): MatchResponseData

    @GET("api/v1/games")
    suspend fun getAllGamesFiltered(@Query("per_page") per_page: Int, @Query("page") page: Int, @Query("postseason") postseason: Boolean): MatchResponse

    @GET("api/v1/games")
    suspend fun getGamesOfTeam(@Query("team_ids[]") teamId: Long, @Query("postseason") postseason: Boolean): MatchResponse

    @GET("api/v1/games")
    suspend fun getGamesOfTeamPaged(@Query("team_ids[]") teamId: Long, @Query("per_page") per_page: Int, @Query("page") page: Int, @Query("postseason") postseason: Boolean): MatchResponse

    @GET("api/v1/games")
    suspend fun getGamesOfTeamSeasonPaged(@Query("team_ids[]") teamId: Long, @Query("per_page") per_page: Int, @Query("page") page: Int, @Query("postseason") postseason: Boolean, @Query("seasons[]") season: Int): MatchResponse

    @GET("api/v1/games")
    suspend fun getGamesOfSeasonPaged(@Query("per_page") per_page: Int, @Query("page") page: Int, @Query("postseason") postseason: Boolean, @Query("seasons[]") season: Int): MatchResponse

    @GET("api/v1/stats")
    suspend fun getFirstSeason(@Query("player_ids[]") playerId: Long, @Query("postseason") postseason: Boolean): StatsResponse

    @GET("api/v1/stats")
    suspend fun getLastSeason(@Query("player_ids[]") playerId: Long, @Query("postseason") postseason: Boolean, @Query("page") page: Int): StatsResponse

    @GET("api/v1/season_averages")
    suspend fun getSeasonAverage(@Query("player_ids[]") playerId: Long, @Query("season") season: Int): SeasonAverageResponse

    @GET("api/v1/stats")
    suspend fun getSeasonStatsPaged(@Query("per_page") per_page: Int, @Query("page") page: Int, @Query("player_ids[]") playerId: Long, @Query("postseason") postseason: Boolean, @Query("seasons[]") season: Int): StatsResponse

    @GET("api/v1/stats")
    suspend fun getGameStats(@Query("game_ids[]") gameId: Long, @Query("per_page") per_page: Int): StatsResponse
}
