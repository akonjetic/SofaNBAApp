package konjetic.sofanbaapp.adapter.playerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.PlayerDetailsHighlightsItemBinding
import konjetic.sofanbaapp.network.model.HighlightResponseData

@SuppressLint("NotifyDataSetChanged")
class PlayerHighlightsAdapter(
    private val context: Context,
    private val highlights: ArrayList<HighlightResponseData>
) : RecyclerView.Adapter<PlayerHighlightsAdapter.HighlightsViewHolder>() {

    fun updateHighlights(updated: ArrayList<HighlightResponseData>) {
        highlights.clear()
        highlights.addAll(updated)
        notifyDataSetChanged()
    }

    class HighlightsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PlayerDetailsHighlightsItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.player_details_highlights_item, parent, false)
        return HighlightsViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighlightsViewHolder, position: Int) {
        val video = highlights[position]

        if (position == 0) {
            holder.binding.highlightsTitle.visibility = View.VISIBLE
        } else {
            holder.binding.highlightsTitle.visibility = View.GONE
        }

        holder.binding.matchTitleName.text = video.name
        holder.binding.youtubePlaceholder.load(
            context.getString(R.string.highlight_placeholder_template, video.url.substringAfter("="))
        )

        holder.binding.root.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.highlight_url_template, video.url, video.startTimestamp)))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return highlights.size
    }
}
