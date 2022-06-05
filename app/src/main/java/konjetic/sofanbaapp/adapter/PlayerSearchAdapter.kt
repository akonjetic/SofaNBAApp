package konjetic.sofanbaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_PLAYER
import konjetic.sofanbaapp.activity.FAVORITE
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.database.NBADatabase
import konjetic.sofanbaapp.databinding.SearchPlayersItemBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayersResponseData
import kotlinx.coroutines.runBlocking

@SuppressLint("NotifyDataSetChanged")
class PlayerSearchAdapter(
    private val context: Context,
    private val playersList: ArrayList<PlayersResponseData>
) : RecyclerView.Adapter<PlayerSearchAdapter.PlayerSearchViewHolder>() {

    fun updateSearchList(updated: ArrayList<PlayersResponseData>) {
        playersList.clear()
        playersList.addAll(updated)
        notifyDataSetChanged()
    }

    class PlayerSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = SearchPlayersItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_players_item, parent, false)
        return PlayerSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerSearchViewHolder, position: Int) {
        val player = playersList[position]
        holder.binding.searchPlayerName.text = PlayerHelper().getFullName(player)

        holder.binding.root.setOnClickListener {
            var favorite = false
            runBlocking {
                if (NBADatabase.getDatabase(context)?.getNBADao()?.checkIfFavoritePlayer(player.id) != null) {
                    favorite = true
                }
            }
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_PLAYER, player)
                putExtra(FAVORITE, favorite)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (playersList.size > 10) {
            10
        } else {
            playersList.size
        }
    }
}
