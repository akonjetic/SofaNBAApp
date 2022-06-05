package konjetic.sofanbaapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.network.Network
import konjetic.sofanbaapp.network.model.*
import konjetic.sofanbaapp.network.paging.PlayerMatchesPagingSource
import kotlinx.coroutines.launch

class PlayerActivityViewModel : ViewModel() {

    val listOfPlayerImages = MutableLiveData<ArrayList<PlayerImgResponseData>>()
    val listOfFavoritePlayers = MutableLiveData<ArrayList<PlayerData>>()
    val seasonsRange = MutableLiveData<ArrayList<Int>>()
    val seasonAverages = MutableLiveData<ArrayList<SeasonAverageResponse>>()
    val allTeams = MutableLiveData<TeamResponse>()
    val playerHighlights = MutableLiveData<ArrayList<HighlightResponseData>>()

    val flowSeasonStats = Pager(PagingConfig(pageSize = 20)) {
        PlayerMatchesPagingSource(Network().getBallDontLieService(), PlayerActivity.playerID, PlayerActivity.postseason, PlayerActivity.season)
    }.flow.cachedIn(viewModelScope)

    fun getPlayerImages(playerId: Long) {
        viewModelScope.launch {
            val request = Network().getSofaScoreAPIService().playerImgSearch(playerId)
            val tempImgs = listOfPlayerImages.value ?: arrayListOf()

            if (request.isSuccessful) {
                tempImgs.addAll(request.body()?.data as ArrayList<PlayerImgResponseData>)
                listOfPlayerImages.value = tempImgs.distinctBy { i -> i.id } as ArrayList<PlayerImgResponseData>
            }
        }
    }

    fun addPlayerImage(data: PlayerImgPost) {
        viewModelScope.launch {
            Network().getSofaScoreAPIService().postPlayerImg(data)
        }
    }

    fun getSeasonRange(playerId: Long) {

        viewModelScope.launch {
            val seasons: ArrayList<Int> = arrayListOf()
            var tempStats = Network().getBallDontLieService().getFirstSeason(playerId, false)
            seasons.add(tempStats.data[0].game.season)
            tempStats = Network().getBallDontLieService().getLastSeason(playerId, false, tempStats.meta.total_pages)
            seasons.add(tempStats.data[tempStats.data.size - 1].game.season)
            seasonsRange.value = seasons
        }
    }

    fun getSeasonAverage(playerId: Long, seasons: ArrayList<Int>) {
        viewModelScope.launch {
            val tempStats: ArrayList<SeasonAverageResponse> = arrayListOf()
            for (item in seasons) {
                try {
                    tempStats.add(Network().getBallDontLieService().getSeasonAverage(playerId, item))
                } catch (e: Exception) {
                    print(e.message)
                }
            }
            seasonAverages.value = tempStats
        }
    }

    fun getAllTeams() {
        viewModelScope.launch {
            allTeams.value = Network().getBallDontLieService().getAllTeams(30, 1)
        }
    }

    fun getHighlightsOfPlayer(playerId: Long) {
        viewModelScope.launch {
            val response = Network().getSofaScoreAPIService().getHighlightsOfPlayer(playerId)

            if (response.isSuccessful) {
                playerHighlights.value = response.body()?.data
            }
        }
    }

    fun removeFavoritePlayerFromDB(context: Context, playerId: Long) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoritePlayerById(playerId)
        }
    }

    fun insertFavoritePlayerToDB(context: Context, player: PlayerData) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoritePlayer(player)
        }
    }

    fun getFavoritePlayers(context: Context) {
        viewModelScope.launch {
            listOfFavoritePlayers.value = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
        }
    }
}
