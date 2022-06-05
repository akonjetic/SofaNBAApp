package konjetic.sofanbaapp.adapter.matchActivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.database.entity.PlayerFavoriteImg
import konjetic.sofanbaapp.databinding.MatchTopPlayerRowBinding
import konjetic.sofanbaapp.network.model.PlayerImgResponseData
import konjetic.sofanbaapp.network.model.StatsResponseData
import kotlinx.coroutines.runBlocking

@SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n")
class MatchTopPlayerAdapter(
    private val context: Context,
    private val topPlayers: ArrayList<StatsResponseData>,
    private var categoryName: String
) : RecyclerView.Adapter<MatchTopPlayerAdapter.TopPlayersViewHolder>() {

    private val imagesList = arrayListOf<PlayerImgResponseData>()

    class TopPlayersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = MatchTopPlayerRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPlayersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.match_top_player_row, parent, false)
        return TopPlayersViewHolder(view)
    }

    fun updateImagesList(updated: ArrayList<PlayerImgResponseData>) {
        imagesList.clear()
        imagesList.addAll(updated)
        notifyDataSetChanged()
    }

    fun updatePlayersList(updated: ArrayList<StatsResponseData>) {
        topPlayers.clear()
        topPlayers.addAll(updated)
        notifyDataSetChanged()
    }

    fun updateCategory(update: String) {
        categoryName = update
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (topPlayers.size > 3) {
            3
        } else {
            topPlayers.size
        }
    }

    override fun onBindViewHolder(holder: TopPlayersViewHolder, position: Int) {
        val player = topPlayers[position]

        val favImg: PlayerFavoriteImg?

        runBlocking {
            favImg = NBADatabase.getDatabase(context)?.getNBADao()?.getPlayerFavoriteImg(player.id)
        }

      /*  if(player.player.id !in imagesList.map {image -> image.playerId } && favImg == null){
            viewModel.getPlayerImages(player.player.id)
        }*/

        holder.binding.index.text = (position + 1).toString()
        holder.binding.score.text = when (categoryName) {
            context.getString(R.string.matchStatTitle1) -> player.fgm.toString()
            context.getString(R.string.matchStatTitle2) -> player.fg3m.toString()
            context.getString(R.string.matchStatTitle3) -> player.reb.toString()
            context.getString(R.string.matchStatTitle4) -> player.ast.toString()
            context.getString(R.string.matchStatTitle5) -> player.turnover.toString()
            else -> player.oreb.toString()
        }
        holder.binding.team.text = player.team.abbreviation

        holder.binding.image.load(
            when (position % 3) {
                1 -> context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_1_small)
                2 -> context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_2_small)
                else -> context.getDrawable(R.drawable.ic_assets_exportable_graphics_player_3_small)
            }
        )

        if (favImg != null) {
            holder.binding.image.load(favImg.imgUrl)
        } else if (imagesList.isNotEmpty() && player.id in imagesList.map { image -> image.playerId }) {
            for (item in imagesList) {
                if (item.playerId == player.player.id) {
                    holder.binding.image.load(item.imageUrl)
                    break
                }
            }
        }

        holder.binding.name.text = player.player.first_name + " " + player.player.last_name
    }
}
