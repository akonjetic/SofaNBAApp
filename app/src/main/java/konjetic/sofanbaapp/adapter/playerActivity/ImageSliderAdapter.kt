package konjetic.sofanbaapp.adapter.playerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import coil.load
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.PlayerImageSliderItemBinding
import konjetic.sofanbaapp.network.model.PlayerImgResponseData

@SuppressLint("InflateParams")
class ImageSliderAdapter(val context: Context, private var imagesList: ArrayList<PlayerImgResponseData>) : PagerAdapter() {
    override fun getCount(): Int {
        return if (imagesList.size > 0) {
            imagesList.size
        } else {
            1
        }
    }

    fun updateImages(updated: ArrayList<PlayerImgResponseData>) {
        imagesList.clear()
        imagesList.addAll(updated)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.player_image_slider_item, null, false)

        val binding = PlayerImageSliderItemBinding.bind(view)
        val imageView = binding.ivImages

        if (imagesList.size > 0) {
            imagesList[position].let {
                imageView.load(it.imageUrl)
            }
        } else {
            imageView.load(R.drawable.ic_assets_exportable_graphics_player_1_small)
        }
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}
