package konjetic.sofanbaapp.network.paging

import androidx.recyclerview.widget.DiffUtil
import konjetic.sofanbaapp.fragment.MatchResponseData

object MatchDiff : DiffUtil.ItemCallback<MatchResponseData>() {
    override fun areItemsTheSame(oldItem: MatchResponseData, newItem: MatchResponseData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MatchResponseData,
        newItem: MatchResponseData
    ): Boolean {
        return oldItem == newItem
    }

}