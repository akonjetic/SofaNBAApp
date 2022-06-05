package konjetic.sofanbaapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import konjetic.sofanbaapp.activity.MainActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.network.Network
import konjetic.sofanbaapp.network.model.*
import konjetic.sofanbaapp.network.paging.PlayerPagingSource
import konjetic.sofanbaapp.network.paging.SeasonMatchPagingFilteredSource
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val listOfSearchedPlayers = MutableLiveData<PlayersResponse>()
    val listOfSearchedTeams = MutableLiveData<ArrayList<TeamInfo>>()
    val listAllTeams = MutableLiveData<TeamResponse>()
    val favoritePlayersFromDB = MutableLiveData<ArrayList<PlayerData>>()
    val favoriteTeamsFromDB = MutableLiveData<ArrayList<TeamData>>()
    val allTeamsFromDB = MutableLiveData<ArrayList<TeamInfo>>()

    val playerImages = MutableLiveData<ArrayList<PlayerImgResponseData>>()

    val flow1 = Pager(PagingConfig(pageSize = 20)) {
        PlayerPagingSource(Network().getBallDontLieService(), this)
    }.flow.cachedIn(viewModelScope)

    val flowSeasonMatches = Pager(PagingConfig(pageSize = 20)) {
        SeasonMatchPagingFilteredSource(Network().getBallDontLieService(), MainActivity.teamId, MainActivity.playoff, MainActivity.season)
    }.flow.cachedIn(viewModelScope)

    fun getListOfSearchedPlayers(search: String) {
        viewModelScope.launch {
            listOfSearchedPlayers.value = Network().getBallDontLieService().playerSearch(search)
        }
    }

    fun insertAllFavoritePlayers(context: Context, list: ArrayList<PlayerData>) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertAllFavoritePlayers(list)
        }
    }

    fun getListOfSearchedTeams(context: Context, search: String) {
        viewModelScope.launch {
            listOfSearchedTeams.value = NBADatabase.getDatabase(context)?.getNBADao()?.getTeamsByName(
                "%$search%"
            ) as ArrayList<TeamInfo>
        }
    }

    fun getFavoritePlayersFromDB(context: Context) {
        viewModelScope.launch {
            favoritePlayersFromDB.value = NBADatabase.getDatabase(context)?.getNBADao()
                ?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
        }
    }

    fun getFavoriteTeamsFromDB(context: Context) {
        viewModelScope.launch {
            favoriteTeamsFromDB.value = NBADatabase.getDatabase(context)?.getNBADao()
                ?.getAllFavoriteTeamsSorted() as ArrayList<TeamData>
        }
    }

    fun getAllTeams() {
        viewModelScope.launch {
            listAllTeams.value = Network().getBallDontLieService().getAllTeams(30, 1)
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

    fun removeFromFavorites(context: Context) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.deleteAllFavoritePlayers()
            NBADatabase.getDatabase(context)?.getNBADao()?.deleteAllFavoriteTeams()
        }
    }

    fun insertFavoritePlayerToDB(context: Context, playerData: PlayerData) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoritePlayer(playerData)
        }
    }

    fun insertFavoriteTeamToDB(context: Context, teamData: TeamData) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoriteTeam(teamData)
        }
    }

    fun insertTeam(context: Context, team: TeamInfo) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertTeam(team)
        }
    }

    fun getAllTeamsFromDB(context: Context) {
        viewModelScope.launch {
            allTeamsFromDB.value = NBADatabase.getDatabase(context)?.getNBADao()?.getAllTeams() as ArrayList<TeamInfo>
        }
    }
}
