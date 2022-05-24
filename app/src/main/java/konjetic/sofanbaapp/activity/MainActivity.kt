package konjetic.sofanbaapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.databinding.ActivityMainBinding
import konjetic.sofanbaapp.fragment.ExploreFragment
import konjetic.sofanbaapp.fragment.FavoritesFragment
import konjetic.sofanbaapp.fragment.SeasonsFragment
import konjetic.sofanbaapp.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
            when(it.itemId){
                R.id.ic_explore -> setCurrentFragment(exploreFragment)
                R.id.ic_favorites -> setCurrentFragment(favoritesFragment)
                R.id.ic_seasons -> setCurrentFragment(seasonsFragment)
                R.id.ic_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }

}