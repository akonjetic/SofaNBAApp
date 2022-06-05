package konjetic.sofanbaapp.adapter.teamActivity

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
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.databinding.TeamMatchItemHeaderBinding
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.helper.DateTimeHelper
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.network.model.TeamResponseData

class TeamMatchPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<MatchResponseData>) :
    PagingDataAdapter<MatchResponseData, TeamMatchPagingAdapter.TeamMatchViewHolder>(diffCallback) {

    class TeamMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TeamMatchItemHeaderBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: TeamMatchViewHolder, position: Int) {
        val match = getItem(position)

        if (position > 0) {
            val month1 = DateTimeHelper().getMonthFromTimeStamp(match?.date!!)
            val month2 = DateTimeHelper().getMonthFromTimeStamp(getItem(position - 1)?.date!!)
            if (month1 != month2) {
                holder.binding.dateHeader.text =
                    DateTimeHelper().getDateMonthAndYearFromTimeStamp(match.date)
                holder.binding.dateHeader.visibility = View.VISIBLE
            } else {
                holder.binding.dateHeader.visibility = View.GONE
            }
        } else {
            holder.binding.dateHeader.visibility = View.VISIBLE
            holder.binding.dateHeader.text =
                DateTimeHelper().getDateMonthAndYearFromTimeStamp(match?.date!!)
        }

        holder.binding.matchResCard.date.day.text = DateTimeHelper().getDayOfWeekLong(match.date)
        holder.binding.matchResCard.date.date.text =
            DateTimeHelper().getDayMonth(match.date).uppercase()

        val opponent: TeamResponseData
        if (match.home_team.id == TeamActivity.team.id) {
            opponent = match.visitor_team
        } else {
            opponent = match.home_team
        }

        holder.binding.matchResCard.opponent.abbrev.text = opponent.abbreviation
        holder.binding.matchResCard.opponent.homeVisitor.text =
            when (opponent.id == match.visitor_team.id) {
                true -> "@"
                else -> context.getString(R.string.teamMatchesHomeOrVisitorChar)
            }

        with(TeamHelper()) {
            holder.binding.matchResCard.opponent.logoImage.editLogoImg(opponent)
        }

        if (opponent.id == match.visitor_team.id) {
            holder.binding.matchResCard.scoreDetail.gameResult.text = when (match.visitor_team_score > match.home_team_score) {
                true -> context.getString(R.string.teamMatchesResultChar2)
                else -> context.getString(R.string.teamMatchesResultChar1)
            }
            holder.binding.matchResCard.scoreDetail.gameResult.isActivated = when (match.visitor_team_score > match.home_team_score) {
                true -> false
                else -> true
            }
        } else {
            holder.binding.matchResCard.scoreDetail.gameResult.text = when (match.visitor_team_score > match.home_team_score) {
                true -> context.getString(R.string.teamMatchesResultChar1)
                else -> context.getString(R.string.teamMatchesResultChar2)
            }
            holder.binding.matchResCard.scoreDetail.gameResult.isActivated = when (match.visitor_team_score > match.home_team_score) {
                true -> true
                else -> false
            }
        }

        holder.binding.matchResCard.scoreDetail.team1.text = when (match.visitor_team.id == opponent.id) {
            true -> match.home_team_score.toString()
            else -> match.visitor_team_score.toString()
        }

        holder.binding.matchResCard.scoreDetail.team2.text = when (match.visitor_team.id == opponent.id) {
            true -> match.visitor_team_score.toString()
            else -> match.home_team_score.toString()
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, MatchActivity::class.java).apply {
                putExtra(EXTRA_MATCH, match)
            }

            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMatchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.team_match_item_header, parent, false)
        return TeamMatchViewHolder(view)
    }
}
