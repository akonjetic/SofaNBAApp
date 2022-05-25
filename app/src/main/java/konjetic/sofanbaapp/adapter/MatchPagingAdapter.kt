package konjetic.sofanbaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.SeasonsMatchItemBinding
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.helper.DateTimeHelper
import konjetic.sofanbaapp.helper.TeamHelper

class MatchPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<MatchResponseData>):
    PagingDataAdapter<MatchResponseData, MatchPagingAdapter.MatchViewHolder>(diffCallback){

        class MatchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val binding = SeasonsMatchItemBinding.bind(itemView)
        }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val game = getItem(position)

        with(TeamHelper()) {
            holder.binding.nbaLogo.editLogoColor(game?.home_team!!)
            holder.binding.nbaLogo2.editLogoColor(game.visitor_team)
            holder.binding.logoImage.editLogoImg(game.home_team)
            holder.binding.logoImage2.editLogoImg(game.visitor_team)
        }

        holder.binding.team1Abbrev.text = game?.home_team?.abbreviation
        holder.binding.team2Abbrev.text = game?.visitor_team?.abbreviation

        holder.binding.team1Result.text = game?.home_team_score.toString()
        holder.binding.team2Result.text = game?.visitor_team_score.toString()

        holder.binding.matchType.text = game?.status

        holder.binding.dateText.text = DateTimeHelper().getDateLongFormat(game?.date!!).uppercase()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.seasons_match_item, parent, false)
        return MatchViewHolder(view)
    }


}