package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_PLAYER
import konjetic.sofanbaapp.activity.FAVORITE
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.databinding.ExplorePlayersItemBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayerImgResponseData
import konjetic.sofanbaapp.network.model.PlayersResponseData
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel
import kotlinx.coroutines.runBlocking

@SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
class ExplorePlayerPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<PlayersResponseData>, val viewModel: MainActivityViewModel) :
    PagingDataAdapter<PlayersResponseData, ExplorePlayerPagingAdapter.PlayerViewHolder>(diffCallback) {

    private var imagesList = ArrayList<PlayerImgResponseData>()

    fun updateImgList(updatedList: ArrayList<PlayerImgResponseData>) {
        imagesList.clear()
        imagesList.addAll(updatedList)
        notifyDataSetChanged()
    }

    class PlayerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val binding = ExplorePlayersItemBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val favList: ArrayList<PlayerData>
        val favImg: PlayerFavoriteImg?

        val player = getItem(position)
        var favPlayer = PlayerData()
        var favPlayers: ArrayList<PlayerData>

        runBlocking {
            favList = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
            favImg = NBADatabase.getDatabase(context)?.getNBADao()?.getPlayerFavoriteImg(player?.id!!)
            favPlayers = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
        }

        for (item in favList) {
            if (item.id == player?.id) {
                holder.binding.playerFavoriteIcon.isActivated = true
                favPlayer = item
                break
            } else {
                holder.binding.playerFavoriteIcon.isActivated = false
            }
        }

        if (player != null) {
            holder.binding.playerName.text = PlayerHelper().getFullName(player)
        }

        holder.binding.playerTeam.text = player?.team?.abbreviation

        val imagePlaceholder = when (position % 3) {
            0 -> R.drawable.ic_assets_exportable_graphics_player_1_small
            1 -> R.drawable.ic_assets_exportable_graphics_player_2_small
            2 -> R.drawable.ic_assets_exportable_graphics_player_3_small
            else -> R.drawable.ic_assets_exportable_graphics_player_1_small
        }

        holder.binding.playerImage.load(imagePlaceholder)

        if (player?.id !in imagesList.map { image -> image.playerId } && favImg == null) {
            viewModel.getPlayerImages(player?.id!!)
        }

        if (favImg != null) {
            holder.binding.playerImage.load(favImg.imgUrl)
        } else if (imagesList.isNotEmpty() && player?.id in imagesList.map { image -> image.playerId }) {
            for (item in imagesList) {
                if (item.playerId == player?.id) {
                    holder.binding.playerImage.load(item.imageUrl)
                    break
                }
            }
        }

        holder.binding.playerFavoriteIcon.setOnClickListener {
            if (holder.binding.playerFavoriteIcon.isActivated) {
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoritePlayer(favPlayer) }
                holder.binding.playerFavoriteIcon.isActivated = false
            } else {
                runBlocking {
                    NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoritePlayer(
                        PlayerData(
                            player?.id!!, player.first_name, player.last_name,
                            player.team.abbreviation, player.height_feet, player.height_inches, player.team.toTeamInfo(),
                            player.weight_pounds, if (favPlayers.size > 0) { favPlayers[favPlayers.size - 1].positionDB + 1 } else { 0 }
                        )
                    )
                }
                holder.binding.playerFavoriteIcon.isActivated = true
            }
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_PLAYER, player)
                putExtra(FAVORITE, holder.binding.playerFavoriteIcon.isActivated)
            }

            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_players_item, parent, false)
        return PlayerViewHolder(view)
    }
}
