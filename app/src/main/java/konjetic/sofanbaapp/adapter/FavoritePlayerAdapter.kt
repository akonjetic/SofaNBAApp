package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.databinding.ExplorePlayersItemBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayerImgResponse
import kotlinx.coroutines.runBlocking

class FavoritePlayerAdapter(
    private val context:Context,
    private val favoritesList: ArrayList<PlayerData>
) : RecyclerView.Adapter<FavoritePlayerAdapter.FavoritePlayerViewHolder>(){

    class FavoritePlayerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ExplorePlayersItemBinding.bind(view)
    }

    var imagesList = ArrayList<PlayerImgResponse>()


    fun updateImgList(updatedList: ArrayList<PlayerImgResponse>){
        imagesList.clear()
        imagesList = updatedList
        notifyDataSetChanged()
    }

    fun updateFavorites(updated: ArrayList<PlayerData>) {
        favoritesList.clear()
        favoritesList.addAll(updated)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_players_item, parent, false)
        return FavoritePlayerViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: FavoritePlayerViewHolder, position: Int) {
        val favorite = favoritesList[position]
        var chosenImages = PlayerImgResponse(arrayListOf())
        val favImg : PlayerFavoriteImg?

        runBlocking {
            favImg = NBADatabase.getDatabase(context)?.getNBADao()?.getPlayerFavoriteImg(favorite.id)
        }

        for(item in imagesList){
            if(item.data[0].playerId == favorite.id){
                chosenImages = item
                holder.binding.playerImage.load(chosenImages.data[0].imageUrl)
              //  notifyDataSetChanged()
                break
            }
        }

        holder.binding.playerFavoriteIcon.isActivated = true

        if(favImg != null){
            holder.binding.playerImage.load(
                favImg.imgUrl
            )
        } else if(chosenImages.data.size > 0){
            holder.binding.playerImage.load(
                chosenImages.data[0].imageUrl

            )
            //chosenImages = PlayerImgResponse(arrayListOf())
        } else if(position % 3 == 0 || position % 3 == 3){
            holder.binding.playerImage.setImageDrawable(
                context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_1_small)
            )
        } else if(position % 3 == 1){
            holder.binding.playerImage.setImageDrawable(
                context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_2_small)
            )
        } else{
            holder.binding.playerImage.setImageDrawable(
                context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_3_small)
            )
        }

        holder.binding.playerName.text = PlayerHelper().getFullNamePD(favorite)
        holder.binding.playerTeam.text = favorite.teamName

        holder.binding.playerFavoriteIcon.setOnClickListener {
            if(holder.binding.playerFavoriteIcon.isActivated){
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoritePlayer(favorite) }
                holder.binding.playerFavoriteIcon.isActivated = false
                updateFavorites(favoritesList.filter { data ->
                    data.id != favorite.id
                } as ArrayList<PlayerData>)
                notifyItemChanged(position)

            }
        }


    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }
}