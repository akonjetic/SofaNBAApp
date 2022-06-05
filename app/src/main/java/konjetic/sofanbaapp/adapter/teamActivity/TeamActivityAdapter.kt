package konjetic.sofanbaapp.adapter.teamActivity

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.fragment.team.TeamDetailsFragment
import konjetic.sofanbaapp.fragment.team.TeamMatchesFragment

private val titles = arrayOf(
    R.string.teamTabTitle1,
    R.string.teamTabTitle2
)

class TeamActivityAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TeamDetailsFragment()
            else -> TeamMatchesFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(titles[position])
    }
}
