package konjetic.sofanbaapp.adapter.playerActivity

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.fragment.player.PlayerDetailsFragment
import konjetic.sofanbaapp.fragment.player.PlayerMatchesFragment
import konjetic.sofanbaapp.fragment.player.PlayerStatisticsFragment

private val titles = arrayOf(
    R.string.playerTabTitle1,
    R.string.playerTabTitle2,
    R.string.playerTabTitle3
)

class PlayerActivityAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PlayerDetailsFragment()
            1 -> PlayerStatisticsFragment()
            else -> PlayerMatchesFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(titles[position])
    }
}
