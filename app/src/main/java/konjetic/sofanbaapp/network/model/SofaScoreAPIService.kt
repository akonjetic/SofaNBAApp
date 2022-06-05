package konjetic.sofanbaapp.network.model

import retrofit2.Response
import retrofit2.http.*

interface SofaScoreAPIService {

    @GET("api/v1/academy/player-image/player/{playerId}")
    suspend fun playerImgSearch(@Path("playerId") playerId: Long): Response<PlayerImgResponse>

    @POST("api/v1/academy/player-image")
    suspend fun postPlayerImg(@Body imageData: PlayerImgPost): Response<Unit>

    @GET("api/v1/academy/highlight/player/{playerId}")
    suspend fun getHighlightsOfPlayer(@Path("playerId") playerId: Long): Response<HighlightResponse>

    @GET("api/v1/academy/highlight/event/{eventId}")
    suspend fun getHighlightsOfEvent(@Path("eventId") eventId: Long): Response<HighlightResponse>

    @POST("api/v1/academy/highlight")
    suspend fun postEventHighlight(@Body highlightResponseDataPost: HighlightResponseDataPost): Response<Unit>
}
