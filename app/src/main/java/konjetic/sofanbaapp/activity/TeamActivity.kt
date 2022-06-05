package konjetic.sofanbaapp.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import konjetic.sofanbaapp.adapter.teamActivity.TeamActivityAdapter
import konjetic.sofanbaapp.databinding.ActivityTeamBinding
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.network.model.TeamResponseData
import konjetic.sofanbaapp.viewmodel.TeamActivityViewModel

const val EXTRA_TEAM = "EXTRA_TEAM"
const val FAVORITE_T = "FAVORITE"

class TeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamBinding
    private val viewModel: TeamActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenTeam = intent?.extras?.getSerializable(EXTRA_TEAM) as TeamResponseData
        var isItFavorite = intent.getBooleanExtra(FAVORITE_T, false)
        var position = 0

        team = chosenTeam

        if (!isItFavorite) {
            viewModel.checkIfFavorite(this, team.id)
        }

        viewModel.favoriteTeam.observe(this) {
            isItFavorite = it
            binding.iconFavourite.isActivated = isItFavorite
        }

        viewModel.getFavoriteTeamsFromDB(this)
        viewModel.favoriteTeams.observe(this) {
            position = it.size
        }

        setSupportActionBar(binding.toolbar)
        binding.toolbarTitle.text = chosenTeam.full_name
        binding.iconFavourite.isActivated = isItFavorite
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.toolbar.setBackgroundColor(resources.getColor(resources.getIdentifier(TeamHelper().getTeamColorName(chosenTeam.name, this), "color", this.packageName)))
        binding.tabLayout.setBackgroundColor(resources.getColor(resources.getIdentifier(TeamHelper().getTeamColorName(chosenTeam.name, this), "color", this.packageName)))

        binding.iconFavourite.setOnClickListener {
            if (isItFavorite) {
                binding.iconFavourite.isActivated = !isItFavorite
                viewModel.removeFromFavorites(this, team.id)
            } else {
                binding.iconFavourite.isActivated = !isItFavorite
                viewModel.insertFavoriteTeam(this, team.toTeamData(position))
            }
        }

        val teamActivityAdapter = TeamActivityAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = teamActivityAdapter
        val tabs: TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
    }

    companion object {
        var team = TeamResponseData(0, "", "", "", "", "", "")
        var postseason = false
    }
}
