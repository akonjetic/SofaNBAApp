package konjetic.sofanbaapp.adapter.teamActivity

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
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.databinding.TeamLogoAbbrBinding
import konjetic.sofanbaapp.helper.TeamHelper

@SuppressLint("NotifyDataSetChanged")
class TeamDivisionAdapter(
    private val context: Context,
    private val teams: ArrayList<TeamInfo>
) : RecyclerView.Adapter<TeamDivisionAdapter.TeamDivisionViewHolder>() {

    fun updateTeamsList(updated: ArrayList<TeamInfo>) {
        teams.clear()
        teams.addAll(updated)
        notifyDataSetChanged()
    }

    class TeamDivisionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TeamLogoAbbrBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamDivisionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.team_logo_abbr, parent, false)
        return TeamDivisionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamDivisionViewHolder, position: Int) {
        val team = teams[position]

        with(TeamHelper()) {
            holder.binding.teamLogo.logoImage.editLogoImg(team.toTeamResponseData())
            holder.binding.teamLogo.logoBackground.editLogoColor(team.toTeamResponseData())
        }

        holder.binding.teamAbbrev.text = team.abbreviation.uppercase()

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, TeamActivity::class.java).apply {
                putExtra(EXTRA_TEAM, team.toTeamResponseData())
                putExtra(FAVORITE_T, false)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return teams.size
    }
}
