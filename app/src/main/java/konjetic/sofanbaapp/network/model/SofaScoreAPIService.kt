package konjetic.sofanbaapp.network.model

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SofaScoreAPIService {

    @GET("api/v1/academy/player-image/player/{playerId}")
    suspend fun playerImgSearch(@Path("playerId") playerId: Long) : PlayerImgResponse

}