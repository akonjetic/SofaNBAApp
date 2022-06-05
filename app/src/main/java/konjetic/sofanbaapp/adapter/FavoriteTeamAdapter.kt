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
import konjetic.sofanbaapp.databinding.ExploreTeamsItemWithHandlerBinding
import konjetic.sofanbaapp.helper.TeamHelper
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("NotifyDataSetChanged")
class FavoriteTeamAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<TeamData>
) : RecyclerView.Adapter<FavoriteTeamAdapter.FavoriteTeamViewHolder>() {

    var reorder = false

    class FavoriteTeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ExploreTeamsItemWithHandlerBinding.bind(view)
    }

    fun updateFavorites(updated: ArrayList<TeamData>) {
        favoritesList.clear()
        favoritesList.addAll(updated)

        notifyDataSetChanged()
    }

    fun updateReorder() {
        reorder = !reorder

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_teams_item_with_handler, parent, false)
        return FavoriteTeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        val favorite = favoritesList[position]

        holder.binding.teamItems.teamFavoriteIcon.isActivated = true
        holder.binding.teamItems.teamName.text = favorite.full_name

        with(TeamHelper()) {
            holder.binding.teamItems.teamImageCard.editLogoColor(favorite.mapToResponse())
            holder.binding.teamItems.teamImage.editLogoImg(favorite.mapToResponse())
        }

        holder.binding.teamItems.teamFavoriteIcon.setOnClickListener {
            if (holder.binding.teamItems.teamFavoriteIcon.isActivated) {
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoriteTeam(favorite) }
                holder.binding.teamItems.teamFavoriteIcon.isActivated = false
                updateFavorites(
                    favoritesList.filter { data ->
                        data.id != favorite.id
                    } as ArrayList<TeamData>
                )
                notifyItemChanged(position)
            }
        }

        if (reorder) {
            holder.binding.editHandler.visibility = View.VISIBLE
        } else {
            holder.binding.editHandler.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, TeamActivity::class.java).apply {
                putExtra(EXTRA_TEAM, favorite.mapToTeamResponseData())
                putExtra(FAVORITE_T, holder.binding.teamItems.teamFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(favoritesList, i, i + 1)
            }
        } else {
            for (i in toPosition until fromPosition) {
                Collections.swap(favoritesList, i, i + 1)
            }
        }

        favoritesList[fromPosition].position = fromPosition
        favoritesList[toPosition].position = toPosition

        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItem(position: Int) {

        runBlocking {
            NBADatabase.getDatabase(context)?.getNBADao()
                ?.deleteFavoriteTeam(favoritesList[position])

            favoritesList.remove(favoritesList[position])

            notifyItemRemoved(position)
        }
    }
}
