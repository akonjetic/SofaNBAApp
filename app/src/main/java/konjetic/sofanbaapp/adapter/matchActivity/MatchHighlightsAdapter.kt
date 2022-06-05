package konjetic.sofanbaapp.adapter.matchActivity

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
import konjetic.sofanbaapp.databinding.MatchHighlightsItemBinding
import konjetic.sofanbaapp.network.model.HighlightResponseData

@SuppressLint("NotifyDataSetChanged")
class MatchHighlightsAdapter(
    private val context: Context,
    private val highlights: ArrayList<HighlightResponseData>
) : RecyclerView.Adapter<MatchHighlightsAdapter.HighlightsViewHolder>() {

    fun updateHighlights(updated: ArrayList<HighlightResponseData>) {
        highlights.clear()
        highlights.addAll(updated)
        notifyDataSetChanged()
    }

    class HighlightsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = MatchHighlightsItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.match_highlights_item, parent, false)
        return HighlightsViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighlightsViewHolder, position: Int) {
        val video = highlights[position]

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
