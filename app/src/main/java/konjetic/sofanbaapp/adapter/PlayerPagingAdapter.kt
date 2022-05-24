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
import konjetic.sofanbaapp.network.model.PlayerImgResponse
import konjetic.sofanbaapp.network.model.PlayersResponseData
import kotlinx.coroutines.runBlocking


class PlayerPagingAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<PlayersResponseData>):
    PagingDataAdapter<PlayersResponseData, PlayerPagingAdapter.PlayerViewHolder>(diffCallback) {

    var imagesList = ArrayList<PlayerImgResponse>()


    fun updateImgList(updatedList: ArrayList<PlayerImgResponse>){
        imagesList.clear()
        imagesList = updatedList
        notifyDataSetChanged()
    }



    class PlayerViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val binding = ExplorePlayersItemBinding.bind(itemView)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val favList : ArrayList<PlayerData>
        val favImg : PlayerFavoriteImg?

        val player = getItem(position)
        var favPlayer = PlayerData()
        var chosenImages = PlayerImgResponse(arrayListOf())


        runBlocking {
            favList = NBADatabase.getDatabase(context)?.getNBADao()?.getAllFavoritePlayersSorted() as ArrayList<PlayerData>
            favImg = NBADatabase.getDatabase(context)?.getNBADao()?.getPlayerFavoriteImg(player?.id!!)
        }

        for(item in favList){
            if(item.id == player?.id){
                holder.binding.playerFavoriteIcon.isActivated = true
                favPlayer = item
                break
            } else{
                holder.binding.playerFavoriteIcon.isActivated = false
            }
        }
        holder.binding.playerName.text = PlayerHelper().getFullName(player!!)
        holder.binding.playerTeam.text = player.team.name

        for(item in imagesList){
            if(item.data[0].playerId == player.id){
                chosenImages = item
                holder.binding.playerImage.load(chosenImages.data[0].imageUrl)
                break
            }
        }

        if(favImg != null){
            holder.binding.playerImage.load(
                favImg.imgUrl
            )
        } else if(chosenImages.data.size > 0){
            holder.binding.playerImage.load(
                chosenImages.data[0].imageUrl

            )
            //chosenImages = PlayerImgResponse(arrayListOf())
        } else when(position % 3){
            0 ->  holder.binding.playerImage.setImageDrawable(context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_1_small))
            1 -> holder.binding.playerImage.setImageDrawable(context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_2_small))
            2 -> holder.binding.playerImage.setImageDrawable(context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_3_small))
            3 ->  holder.binding.playerImage.setImageDrawable(context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_1_small))
        }

        holder.binding.playerFavoriteIcon.setOnClickListener {
            if(holder.binding.playerFavoriteIcon.isActivated){
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.deleteFavoritePlayer(favPlayer) }
                holder.binding.playerFavoriteIcon.isActivated = false
            } else{
                runBlocking { NBADatabase.getDatabase(context)?.getNBADao()?.insertFavoritePlayer(
                    PlayerData(player.id, player.first_name, player.last_name,
                        player.team.abbreviation, if(favList.size > 0){favList[favList.size-1].position+1}else{0}, player.team.full_name)
                ) }
                holder.binding.playerFavoriteIcon.isActivated = true
            }
        }

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_PLAYER, player)
                putExtra(FAVORITE, holder.binding.playerFavoriteIcon.isActivated)
                }


            context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.explore_players_item, parent, false)
        return PlayerPagingAdapter.PlayerViewHolder(view)
    }



}