package konjetic.sofanbaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.SearchPlayersItemBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayersResponseData

class PlayerSearchAdapter(
    private val context: Context,
    private val playersList: ArrayList<PlayersResponseData>
) : RecyclerView.Adapter<PlayerSearchAdapter.PlayerSearchViewHolder>(){

    fun updateSearchList(updated: ArrayList<PlayersResponseData>){
        playersList.clear()
        playersList.addAll(updated)
        notifyDataSetChanged()
    }

    class PlayerSearchViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = SearchPlayersItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_players_item, parent, false)
        return PlayerSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerSearchViewHolder, position: Int) {
        val player = playersList[position]
        holder.binding.searchPlayerName.text = PlayerHelper().getFullName(player)
    }

    override fun getItemCount(): Int {
        return if (playersList.size > 10){
            10
        } else {
            playersList.size
        }
    }


}