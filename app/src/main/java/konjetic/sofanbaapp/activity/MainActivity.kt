package konjetic.sofanbaapp.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.ActivityMainBinding
import konjetic.sofanbaapp.fragment.ExploreFragment
import konjetic.sofanbaapp.fragment.FavoritesFragment
import konjetic.sofanbaapp.fragment.SeasonsFragment
import konjetic.sofanbaapp.fragment.SettingsFragment
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val exploreFragment = ExploreFragment()
        val favoritesFragment = FavoritesFragment()
        val seasonsFragment = SeasonsFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(exploreFragment)

        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_explore -> setCurrentFragment(exploreFragment)
                R.id.ic_favorites -> setCurrentFragment(favoritesFragment)
                R.id.ic_seasons -> setCurrentFragment(seasonsFragment)
                R.id.ic_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }

        viewModel.getAllTeams()
        viewModel.listAllTeams.observe(this) { teams ->
            for (team in teams.data) {
                viewModel.insertTeam(this, team.toTeamInfo())
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }

    companion object {
        var playoff = false
        var season = 0
        var teamId = 0L
        var teamName = ""
    }
}
