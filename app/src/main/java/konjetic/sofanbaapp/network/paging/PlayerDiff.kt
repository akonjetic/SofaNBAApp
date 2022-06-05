package konjetic.sofanbaapp.network.paging

import androidx.recyclerview.widget.DiffUtil
import konjetic.sofanbaapp.network.model.PlayersResponseData

object PlayerDiff : DiffUtil.ItemCallback<PlayersResponseData>() {

    override fun areItemsTheSame(
        oldItem: PlayersResponseData,
        newItem: PlayersResponseData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PlayersResponseData,
        newItem: PlayersResponseData
    ): Boolean {
        return oldItem == newItem
    }
}
