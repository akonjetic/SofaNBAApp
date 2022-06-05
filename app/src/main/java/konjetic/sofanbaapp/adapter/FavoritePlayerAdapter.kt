package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_PLAYER
import konjetic.sofanbaapp.activity.FAVORITE
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.databinding.ExplorePlayersItemWithHandlerBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayerImgResponseData
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("NotifyDataSetChanged")
class FavoritePlayerAdapter(
    private val context: Context,
    private val favoritesList: ArrayList<PlayerData>
) : RecyclerView.Adapter<FavoritePlayerAdapter.FavoritePlayerViewHolder>() {

    class FavoritePlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ExplorePlayersItemWithHandlerBinding.bind(view)
    }

    private val imagesList = ArrayList<PlayerImgResponseData>()
    var reorder = false

    fun updateImagesList(updatedList: ArrayList<PlayerImgResponseData>) {
        imagesList.clear()
        imagesList.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun updateFavorites(updated: ArrayList<PlayerData>) {
        favoritesList.clear()
        favoritesList.addAll(updated)

        notifyDataSetChanged()
    }

    fun updateReorder() {
        reorder = !reorder

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_players_item_with_handler, parent, false)
        return FavoritePlayerViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: FavoritePlayerViewHolder, position: Int) {

        val favorite = favoritesList[position]
        val favImg: PlayerFavoriteImg?

        runBlocking {
            favImg = NBADatabase.getDatabase(context)?.getNBADao()?.getPlayerFavoriteImg(favorite.id)
        }

        holder.binding.playerItem.playerFavoriteIcon.isActivated = true

        val imagePlaceholder = when (position % 3) {
            0 -> R.drawable.ic_assets_exportable_graphics_player_1_small
            1 -> R.drawable.ic_assets_exportable_graphics_player_2_small
            2 -> R.drawable.ic_assets_exportable_graphics_player_3_small
            else -> R.drawable.ic_assets_exportable_graphics_player_1_small
        }

        holder.binding.playerItem.playerImage.load(imagePlaceholder)

        if (favImg != null) {
            holder.binding.playerItem.playerImage.load(favImg.imgUrl)
        } else if (imagesList.isNotEmpty() && favorite.id in imagesList.map { p -> p.playerId }) {
            for (item in imagesList) {
                if (item.playerId == favorite.id) {
                    holder.binding.playerItem.playerImage.load(item.imageUrl)
                    break
                }
            }
        }

        holder.binding.playerItem.playerName.text = PlayerHelper().getFullNamePD(favorite)
        holder.binding.playerItem.playerTeam.text = favorite.team.abbreviation

        holder.binding.playerItem.playerFavoriteIcon.setOnClickListener {
            if (holder.binding.playerItem.playerFavoriteIcon.isActivated) {
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoritePlayer(favorite) }
                holder.binding.playerItem.playerFavoriteIcon.isActivated = false
                updateFavorites(
                    favoritesList.filter { data ->
                        data.id != favorite.id
                    } as ArrayList<PlayerData>
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
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_PLAYER, favorite.toResponseData())
                putExtra(FAVORITE, holder.binding.playerItem.playerFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    fun removeItem(position: Int) {

        runBlocking {
            NBADatabase.getDatabase(context)?.getNBADao()
                ?.deleteFavoritePlayer(favoritesList[position])

            favoritesList.remove(favoritesList[position])

            notifyItemRemoved(position)
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
        favoritesList[fromPosition].positionDB = fromPosition
        favoritesList[toPosition].positionDB = toPosition

        notifyItemMoved(fromPosition, toPosition)
    }
}
