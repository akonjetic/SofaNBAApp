package konjetic.sofanbaapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import konjetic.sofanbaapp.databinding.ActivityTeamBinding
import konjetic.sofanbaapp.network.model.TeamResponseData

const val EXTRA_TEAM = "EXTRA_TEAM"
const val FAVORITE_T = "FAVORITE"

class TeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamBinding
    private val viewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenTeam = intent?.extras?.getSerializable(EXTRA_TEAM) as TeamResponseData
        var isItFavorite = intent.getBooleanExtra(FAVORITE_T, false)

        binding.toolbarTitle.text = chosenTeam.full_name
        binding.iconFavourite.isActivated = isItFavorite


    }
}