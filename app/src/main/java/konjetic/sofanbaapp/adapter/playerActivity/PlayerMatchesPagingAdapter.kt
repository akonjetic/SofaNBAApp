package konjetic.sofanbaapp.adapter.playerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_MATCH
import konjetic.sofanbaapp.activity.MatchActivity
import konjetic.sofanbaapp.databinding.MatchItemHeaderCardStatsBinding
import konjetic.sofanbaapp.helper.DateTimeHelper
import konjetic.sofanbaapp.helper.MatchHelper
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.network.model.StatsResponseData
import konjetic.sofanbaapp.network.model.TeamResponse

@SuppressLint("NotifyDataSetChanged")
class PlayerMatchesPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<StatsResponseData>, private var allTeams: TeamResponse) :
    PagingDataAdapter<StatsResponseData, PlayerMatchesPagingAdapter.MatchesViewHolder>(diffCallback) {

    class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = MatchItemHeaderCardStatsBinding.bind(view)
    }

    fun updateTeamsList(updated: TeamResponse) {
        allTeams = updated
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        val homeOrVisitor: Boolean
        val match = getItem(position)?.game
        val myTeam = getItem(position)?.team

        val opponentId: Long

        if (myTeam?.id == match?.home_team_id) {
            opponentId = match?.visitor_team_id!!
            homeOrVisitor = true
        } else {
            opponentId = match?.home_team_id!!
            homeOrVisitor = false
        }

        val opponent = TeamHelper().getTeamResponseData(opponentId, allTeams)

        if (position > 0) {
            val month1 = DateTimeHelper().getMonthFromTimeStamp(match.date)
            val month2 = DateTimeHelper().getMonthFromTimeStamp(getItem(position - 1)?.game?.date!!)
            if (month1 != month2) {
                holder.binding.headerTime.month.text = DateTimeHelper().getDateMonthAndYearFromTimeStamp(match.date)
                holder.binding.headerTime.month.visibility = View.VISIBLE
            } else {
                holder.binding.headerTime.month.visibility = View.GONE
            }
        } else {
            holder.binding.headerTime.month.visibility = View.VISIBLE
            holder.binding.headerTime.month.text = DateTimeHelper().getDateMonthAndYearFromTimeStamp(match.date)
        }

        with(TeamHelper()) {
            if (homeOrVisitor) {
                holder.binding.matchCard.firstTeam.logoImage.editLogoImg(myTeam!!)
                holder.binding.matchCard.secondTeam.logoImage.editLogoImg(opponent)
                holder.binding.matchCard.firstTeam.logoBackground.editLogoColor(myTeam)
                holder.binding.matchCard.secondTeam.logoBackground.editLogoColor(opponent)

                holder.binding.matchCard.firstTeamScore.text = match.home_team_score.toString()
                holder.binding.matchCard.firstTeamScore.isActivated = MatchHelper().getWinner(match.home_team_score, match.visitor_team_score)
                holder.binding.matchCard.secondTeamScore.text = match.visitor_team_score.toString()
                holder.binding.matchCard.secondTeamScore.isActivated = MatchHelper().getWinner(match.visitor_team_score, match.home_team_score)

                holder.binding.matchCard.firstTeamName.text = myTeam.abbreviation.uppercase()
                holder.binding.matchCard.secondTeamName.text = opponent.abbreviation.uppercase()
            } else {
                holder.binding.matchCard.firstTeam.logoImage.editLogoImg(opponent)
                holder.binding.matchCard.secondTeam.logoImage.editLogoImg(myTeam!!)
                holder.binding.matchCard.firstTeam.logoBackground.editLogoColor(opponent)
                holder.binding.matchCard.secondTeam.logoBackground.editLogoColor(myTeam)

                holder.binding.matchCard.firstTeamScore.text = match.visitor_team_score.toString()
                holder.binding.matchCard.firstTeamScore.isActivated = MatchHelper().getWinner(match.visitor_team_score, match.home_team_score)
                holder.binding.matchCard.secondTeamScore.text = match.home_team_score.toString()
                holder.binding.matchCard.secondTeamScore.isActivated = MatchHelper().getWinner(match.home_team_score, match.visitor_team_score)

                holder.binding.matchCard.firstTeamName.text = opponent.abbreviation.uppercase()
                holder.binding.matchCard.secondTeamName.text = myTeam.abbreviation.uppercase()
            }

            holder.binding.root.setOnClickListener {
                val intent = Intent(context, MatchActivity::class.java).apply {
                    if (homeOrVisitor) {
                        putExtra(EXTRA_MATCH, match.toMatchResponseData(myTeam, opponent))
                    } else {
                        putExtra(EXTRA_MATCH, match.toMatchResponseData(opponent, myTeam))
                    }
                }
                context.startActivity(intent)
            }
        }

        val date = DateTimeHelper().getDateLongFormat(match.date)
        holder.binding.matchCard.date.text = date
        holder.binding.matchCard.finalLabel.text = match.status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.match_item_header_card_stats, parent, false)
        return MatchesViewHolder(view)
    }
}
