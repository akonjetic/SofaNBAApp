package konjetic.sofanbaapp.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.network.Network
import konjetic.sofanbaapp.network.model.PlayerImgResponse
import konjetic.sofanbaapp.network.model.PlayersResponse
import konjetic.sofanbaapp.network.model.TeamResponse
import konjetic.sofanbaapp.network.paging.PlayerPagingSource
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val listOfSearchedPlayers = MutableLiveData<PlayersResponse>()
    val listAllTeams = MutableLiveData<TeamResponse>()
    val favoritePlayersFromDB = MutableLiveData<ArrayList<PlayerData>>()
    val favoriteTeamsFromDB = MutableLiveData<ArrayList<TeamData>>()

    val playerImages = MutableLiveData<ArrayList<PlayerImgResponse>>()
    private val tempPlayerImages = arrayListOf<PlayerImgResponse>()


    val flow1 = Pager(PagingConfig(pageSize = 20)){
        PlayerPagingSource(Network().getBallDontLieService(), this)
    }.flow.cachedIn(viewModelScope)


    fun getListOfSearchedPlayers(search: String){
        viewModelScope.launch {
            listOfSearchedPlayers.value = Network().getBallDontLieService().playerSearch(search)
        }
    }

    fun getFavoritePlayersFromDB(context: Context){
        viewModelScope.launch {
            favoritePlayersFromDB.value = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
        }
    }

    fun getFavoriteTeamsFromDB(context: Context){
        viewModelScope.launch {
            favoriteTeamsFromDB.value = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoriteTeamsSorted() as ArrayList<TeamData>
        }
    }

    fun getAllTeams(){
        viewModelScope.launch {
            listAllTeams.value = Network().getBallDontLieService().getAllTeams(30, 1)
        }
    }

    fun getPlayerImages(id: Long){
        viewModelScope.launch {
            try {
                tempPlayerImages.add(Network().getSofaScoreAPIService().playerImgSearch(id))


           } catch(e: Exception){
                print(e.message)

            } finally {
                if(tempPlayerImages.size > 0)
                    playerImages.value = tempPlayerImages
            }

        }
    }


}