package konjetic.sofanbaapp.network.paging

import androidx.recyclerview.widget.DiffUtil
import konjetic.sofanbaapp.network.model.StatsResponseData

object StatsDiff : DiffUtil.ItemCallback<StatsResponseData>() {
    override fun areItemsTheSame(oldItem: StatsResponseData, newItem: StatsResponseData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: StatsResponseData,
        newItem: StatsResponseData
    ): Boolean {
        return oldItem == newItem
    }
}
