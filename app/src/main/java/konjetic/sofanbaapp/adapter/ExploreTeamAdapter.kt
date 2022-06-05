package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_TEAM
import konjetic.sofanbaapp.activity.FAVORITE_T
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.databinding.ExploreTeamsItemBinding
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.network.model.TeamResponseData
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("NotifyDataSetChanged")
class ExploreTeamAdapter(
    private val context: Context,
    private val teams: ArrayList<TeamResponseData>
) : RecyclerView.Adapter<ExploreTeamAdapter.TeamViewHolder>() {

    fun updateTeamList(updated: ArrayList<TeamResponseData>) {
        teams.clear()
        teams.addAll(updated)
        notifyDataSetChanged()
    }

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ExploreTeamsItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_teams_item, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        var favPlayer = TeamData()
        val favList: ArrayList<TeamData>

        runBlocking {
            favList = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoriteTeamsSorted() as ArrayList<TeamData>
        }

        for (item in favList) {
            if (item.id == team.id) {
                holder.binding.teamFavoriteIcon.isActivated = true
                favPlayer = item
                break
            } else {
                holder.binding.teamFavoriteIcon.isActivated = false
            }
        }

        holder.binding.teamName.text = team.full_name
        with(TeamHelper()) {
            holder.binding.teamImageCard.editLogoColor(team)
            holder.binding.teamImage.editLogoImg(team)
        }

        holder.binding.teamFavoriteIcon.setOnClickListener {
            if (holder.binding.teamFavoriteIcon.isActivated) {
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoriteTeam(favPlayer) }
                holder.binding.teamFavoriteIcon.isActivated = false
            } else {
                var size = 0
                runBlocking {
                    if (!NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoriteTeamsSorted().isNullOrEmpty()) {
                        size = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoriteTeamsSorted()?.size!!
                    }
                }
                runBlocking {
                    NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoriteTeam(
                        TeamData(
                            team.id, team.abbreviation, team.city, team.conference, team.division,
                            team.full_name, team.name, if (favList.size > 0) { size } else { 0 }
                        )
                    )
                }
                holder.binding.teamFavoriteIcon.isActivated = true
            }
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, TeamActivity::class.java).apply {
                putExtra(EXTRA_TEAM, team)
                putExtra(FAVORITE_T, holder.binding.teamFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return teams.size
    }
}
