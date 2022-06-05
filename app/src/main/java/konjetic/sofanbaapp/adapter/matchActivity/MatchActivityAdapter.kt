package konjetic.sofanbaapp.adapter.matchActivity

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.fragment.match.MatchDetailsFragment
import konjetic.sofanbaapp.fragment.match.MatchTopPlayersFragment

private val titles = arrayOf(
    R.string.matchTabTitle1,
    R.string.matchTabTitle2
)

class MatchActivityAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MatchDetailsFragment()
            else -> MatchTopPlayersFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(titles[position])
    }
}
