package konjetic.sofanbaapp.network

import konjetic.sofanbaapp.network.model.BallDontLieService
import konjetic.sofanbaapp.network.model.SofaScoreAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Network {

    private val ballDontLieService: BallDontLieService
    private val ballDontLieUrl = "https://www.balldontlie.io/"

    private val sofaScoreAPIService: SofaScoreAPIService
    private val sofaScoreAPIUrl = "http://academy-2022.dev.sofascore.com/"

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        val retrofitBallDontLie = Retrofit.Builder().baseUrl(ballDontLieUrl).addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build()).build()

        ballDontLieService = retrofitBallDontLie.create(BallDontLieService::class.java)

        val retrofitSofaScoreAPI = Retrofit.Builder().baseUrl(sofaScoreAPIUrl).addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build()).build()

        sofaScoreAPIService = retrofitSofaScoreAPI.create(SofaScoreAPIService::class.java)
    }

    fun getBallDontLieService(): BallDontLieService = ballDontLieService
    fun getSofaScoreAPIService(): SofaScoreAPIService = sofaScoreAPIService
}
