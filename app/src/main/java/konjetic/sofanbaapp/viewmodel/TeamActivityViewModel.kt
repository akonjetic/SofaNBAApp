package konjetic.sofanbaapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.network.Network
import konjetic.sofanbaapp.network.paging.TeamMatchPagingSource
import konjetic.sofanbaapp.network.paging.TeamMatchPagingSourcePlayoffs
import kotlinx.coroutines.launch

class TeamActivityViewModel : ViewModel() {

    val teamsOfDivision = MutableLiveData<ArrayList<TeamInfo>>()
    val favoriteTeam = MutableLiveData<Boolean>()
    val favoriteTeams = MutableLiveData<ArrayList<TeamData>>()

    fun checkIfFavorite(context: Context, teamId: Long) {
        viewModelScope.launch {
            val team = NBADatabase.getDatabase(context)?.getNBADao()?.checkIfFavoriteTeam(teamId)

            favoriteTeam.value = team != null
        }
    }

    fun removeFromFavorites(context: Context, teamId: Long) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoriteTeamById(teamId)
        }
    }

    fun insertFavoriteTeam(context: Context, team: TeamData) {
        viewModelScope.launch {
            NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoriteTeam(team)
        }
    }

    fun getTeamsOfDivison(context: Context, division: String) {
        viewModelScope.launch {
            teamsOfDivision.value = NBADatabase.getDatabase(context)?.getNBADao()?.getTeamsFromDivision(division) as ArrayList<TeamInfo>
        }
    }

    fun getFavoriteTeamsFromDB(context: Context) {
        viewModelScope.launch {
            favoriteTeams.value = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoriteTeamsSorted() as ArrayList<TeamData>
        }
    }

    val flow = Pager(PagingConfig(pageSize = 25)) {
        TeamMatchPagingSource(Network().getBallDontLieService(), TeamActivity.team.id)
    }.flow.cachedIn(viewModelScope)

    val flow2 = Pager(PagingConfig(pageSize = 25)) {
        TeamMatchPagingSourcePlayoffs(Network().getBallDontLieService(), TeamActivity.team.id)
    }.flow.cachedIn(viewModelScope)
}
