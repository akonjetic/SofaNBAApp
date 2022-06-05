package konjetic.sofanbaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import konjetic.sofanbaapp.network.Network
import konjetic.sofanbaapp.network.model.*
import kotlinx.coroutines.launch

class MatchActivityViewModel : ViewModel() {

    val gameStats = MutableLiveData<StatsResponse>()
    val eventHighlights = MutableLiveData<ArrayList<HighlightResponseData>>()
    val playerImages = MutableLiveData<ArrayList<PlayerImgResponseData>>()

    fun getGameStats(gameId: Long) {
        viewModelScope.launch {
            gameStats.value = Network().getBallDontLieService().getGameStats(gameId, 100)
        }
    }

    fun getMatchHighlights(matchId: Long) {
        viewModelScope.launch {
            val response = Network().getSofaScoreAPIService().getHighlightsOfEvent(matchId)
            val tempHighlights = eventHighlights.value ?: arrayListOf()

            if (response.isSuccessful) {
                tempHighlights.addAll(response.body()?.data as ArrayList<HighlightResponseData>)
                eventHighlights.value = tempHighlights.distinctBy { i -> i.id } as ArrayList<HighlightResponseData>
            }
        }
    }

    fun postMatchHighlights(data: HighlightResponseDataPost) {
        viewModelScope.launch {
            Network().getSofaScoreAPIService().postEventHighlight(data)
        }
    }

    fun getPlayerImages(playerId: Long) {
        viewModelScope.launch {
            val request = Network().getSofaScoreAPIService().playerImgSearch(playerId)
            val tempImgs = playerImages.value ?: arrayListOf()

            if (request.isSuccessful) {
                tempImgs.addAll(request.body()?.data as ArrayList<PlayerImgResponseData>)
                playerImages.value = tempImgs
            }
        }
    }
}
