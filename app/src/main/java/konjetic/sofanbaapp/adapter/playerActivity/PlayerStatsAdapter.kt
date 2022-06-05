package konjetic.sofanbaapp.adapter.playerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.StatsTableRowBinding

@SuppressLint("NotifyDataSetChanged")
class PlayerStatsAdapter(
    private val context: Context,
    private val stats: ArrayList<String>
) : RecyclerView.Adapter<PlayerStatsAdapter.StatsViewHolder>() {

    fun updateData(updated: ArrayList<String>) {
        stats.clear()
        stats.addAll(updated)

        notifyDataSetChanged()
    }

    class StatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = StatsTableRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.stats_table_row, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val label = stats[position]
        val season1 = stats[position + 19]
        val season2 = stats[position + 38]
        val season3 = stats[position + 57]
        val season4 = stats[position + 76]

        holder.binding.statLabel.text = label
        holder.binding.s1Stat.text = season1
        holder.binding.s2Stat.text = season2
        holder.binding.s3Stat.text = season3
        holder.binding.s4Stat.text = season4

        if (label != "MIN") {
            val biggest = getTheBiggestOne(season1, season2, season3, season4)
            if (season1.toDoubleOrNull() == biggest) {
                holder.binding.s1Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s1CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season2.toDoubleOrNull() == biggest) {
                holder.binding.s2Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s2CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season3.toDoubleOrNull() == biggest) {
                holder.binding.s3Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s3CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season4.toDoubleOrNull() == biggest) {
                holder.binding.s4Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s4CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
        } else {
            val biggest = getLongestMin(season1, season2, season3, season4)
            if (season1 != "N/A" && calculateMins(season1) == biggest) {
                holder.binding.s1Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s1CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season2 != "N/A" && calculateMins(season2) == biggest) {
                holder.binding.s2Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s2CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season3 != "N/A" && calculateMins(season3) == biggest) {
                holder.binding.s3Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s3CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
            if (season4 != "N/A" && calculateMins(season4) == biggest) {
                holder.binding.s4Stat.setTextColor(context.getColor(R.color.color_primary))
                holder.binding.s4CV.setCardBackgroundColor(context.getColor(R.color.color_primary_hightlight))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (stats.size < 19) {
            stats.size
        } else {
            19
        }
    }

    private fun calculateMins(value: String): Double {
        val time: Double

        val min: Double = value.substringBefore(":").toDouble()
        val sec: Double = value.substringAfter(":").toDouble()
        time = min + (sec / 60)

        return time
    }

    private fun getLongestMin(s1: String, s2: String, s3: String, s4: String): Double {
        val array: ArrayList<Double> = arrayListOf()

        var time: Double

        if (s1 != "N/A") {
            time = calculateMins(s1)
            array.add(time)
        }
        if (s2 != "N/A") {
            time = calculateMins(s2)
            array.add(time)
        }
        if (s3 != "N/A") {
            time = calculateMins(s3)
            array.add(time)
        }
        if (s4 != "N/A") {
            time = calculateMins(s4)
            array.add(time)
        }

        array.sort()

        return array[array.size - 1]
    }

    private fun getTheBiggestOne(s1: String, s2: String, s3: String, s4: String): Double {
        val s1Int = if (s1 != "" && s1 != "N/A") {
            s1.toDouble()
        } else {
            0.00
        }

        val s2Int = if (s2 != "" && s2 != "N/A") {
            s2.toDouble()
        } else {
            0.00
        }

        val s3Int = if (s3 != "" && s3 != "N/A") {
            s3.toDouble()
        } else {
            0.00
        }

        val s4Int = if (s4 != "" && s4 != "N/A") {
            s4.toDouble()
        } else {
            0.00
        }

        val scores: ArrayList<Double> = arrayListOf()
        scores.add(s1Int)
        scores.add(s2Int)
        scores.add(s3Int)
        scores.add(s4Int)

        scores.sort()

        return scores[3]
    }
}
