package konjetic.sofanbaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.databinding.ExploreTeamsItemBinding
import konjetic.sofanbaapp.helper.TeamHelper
import kotlinx.coroutines.runBlocking

class FavoriteTeamAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<TeamData>
) : RecyclerView.Adapter<FavoriteTeamAdapter.FavoriteTeamViewHolder>(){

    class FavoriteTeamViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ExploreTeamsItemBinding.bind(view)
    }

    fun updateFavorites(updated: ArrayList<TeamData>){
        favoritesList.clear()
        favoritesList.addAll(updated)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_teams_item, parent, false)
        return FavoriteTeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        val favorite = favoritesList[position]

        holder.binding.teamFavoriteIcon.isActivated = true
        holder.binding.teamName.text = favorite.full_name

        with(TeamHelper()) {
            holder.binding.teamImageCard.editLogoColor(favorite.mapToResponse())
            holder.binding.teamImage.editLogoImg(favorite.mapToResponse())
        }

        holder.binding.teamFavoriteIcon.setOnClickListener {
            if(holder.binding.teamFavoriteIcon.isActivated){
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoriteTeam(favorite) }
                holder.binding.teamFavoriteIcon.isActivated = false
                updateFavorites(favoritesList.filter { data ->
                    data.id != favorite.id
                } as ArrayList<TeamData>)
                notifyItemChanged(position)
            }
        }


    }

    override fun getItemCount(): Int {
        return  favoritesList.size
    }
}