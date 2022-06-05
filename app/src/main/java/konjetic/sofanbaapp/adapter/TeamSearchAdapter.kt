package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.*
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.databinding.SearchPlayersItemBinding
import konjetic.sofanbaapp.helper.TeamHelper
import kotlinx.coroutines.runBlocking

@SuppressLint("NotifyDataSetChanged")
class TeamSearchAdapter(
    private val context: Context,
    private val teamsList: ArrayList<TeamInfo>
) : RecyclerView.Adapter<TeamSearchAdapter.PlayerSearchViewHolder>() {

    fun updateSearchList(updated: ArrayList<TeamInfo>) {
        teamsList.clear()
        teamsList.addAll(updated)
        notifyDataSetChanged()
    }

    class PlayerSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = SearchPlayersItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_players_item, parent, false)
        return PlayerSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerSearchViewHolder, position: Int) {
        val team = teamsList[position]
        holder.binding.searchPlayerName.text = team.full_name

        var favorite = false
        runBlocking {
            if (NBADatabase.getDatabase(context)?.getNBADao()?.checkIfFavoriteTeam(team.id) != null) {
                favorite = true
            }
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, TeamActivity::class.java).apply {
                with(TeamHelper()) {
                    putExtra(EXTRA_TEAM, team.mapToResponse())
                    putExtra(FAVORITE_T, favorite)
                }
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (teamsList.size > 10) {
            10
        } else {
            teamsList.size
        }
    }
}
