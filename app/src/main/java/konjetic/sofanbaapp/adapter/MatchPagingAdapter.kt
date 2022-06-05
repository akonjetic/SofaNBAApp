package konjetic.sofanbaapp.adapter

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
import konjetic.sofanbaapp.databinding.MatchItemHeaderCardBinding
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.helper.DateTimeHelper
import konjetic.sofanbaapp.helper.MatchHelper
import konjetic.sofanbaapp.helper.TeamHelper

class MatchPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<MatchResponseData>) :
    PagingDataAdapter<MatchResponseData, MatchPagingAdapter.MatchViewHolder>(diffCallback) {

    class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MatchItemHeaderCardBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val game = getItem(position)

        if (position == 0) {
            holder.binding.headerDate.month.visibility = View.VISIBLE
            holder.binding.headerDate.month.text = DateTimeHelper().getDateMonthAndYearFromTimeStamp(game?.date!!)
        } else {
            val month1 = DateTimeHelper().getMonthFromTimeStamp(game?.date!!)
            val month2 = DateTimeHelper().getMonthFromTimeStamp(getItem(position - 1)?.date!!)
            if (month1 != month2) {
                holder.binding.headerDate.month.visibility = View.VISIBLE
                holder.binding.headerDate.month.text =
                    DateTimeHelper().getDateMonthAndYearFromTimeStamp(game.date)
            } else {
                holder.binding.headerDate.month.visibility = View.GONE
            }
        }

        if (MatchHelper().getWinner(game.home_team_score, game.visitor_team_score)) {
            holder.binding.matchCard.firstTeamScore.isActivated = true
            holder.binding.matchCard.secondTeamScore.isActivated = false
        } else {
            holder.binding.matchCard.firstTeamScore.isActivated = false
            holder.binding.matchCard.secondTeamScore.isActivated = true
        }

        with(TeamHelper()) {
            holder.binding.matchCard.firstTeam.logoBackground.editLogoColor(game.home_team)
            holder.binding.matchCard.secondTeam.logoBackground.editLogoColor(game.visitor_team)
            holder.binding.matchCard.firstTeam.logoImage.editLogoImg(game.home_team)
            holder.binding.matchCard.secondTeam.logoImage.editLogoImg(game.visitor_team)
        }

        holder.binding.matchCard.firstTeamAbb.text = game.home_team.abbreviation
        holder.binding.matchCard.secondTeamAbb.text = game.visitor_team.abbreviation

        holder.binding.matchCard.firstTeamScore.text = game.home_team_score.toString()
        holder.binding.matchCard.secondTeamScore.text = game.visitor_team_score.toString()

        holder.binding.matchCard.finalLabel.text = game.status

        holder.binding.matchCard.date.text = DateTimeHelper().getDateLongFormat(game.date).uppercase()

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, MatchActivity::class.java).apply {
                putExtra(EXTRA_MATCH, game)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.match_item_header_card, parent, false)
        return MatchViewHolder(view)
    }
}
