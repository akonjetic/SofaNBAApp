package konjetic.sofanbaapp.network.model

import retrofit2.http.GET
import retrofit2.http.Query

interface BallDontLieService {

    @GET("api/v1/players")
    suspend fun playerSearch(@Query("search") search: String) : PlayersResponse

    @GET("api/v1/players")
    suspend fun getAllPlayers(@Query("per_page") per_page: Int, @Query("page") page: Int) : PlayersResponse

    @GET("api/v1/teams")
    suspend fun getAllTeams(@Query("per_page") per_page: Int, @Query("page") page: Int) : TeamResponse



}