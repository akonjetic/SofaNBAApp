package konjetic.sofanbaapp.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import konjetic.sofanbaapp.adapter.playerActivity.PlayerActivityAdapter
import konjetic.sofanbaapp.databinding.ActivityPlayerBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayersResponseData
import konjetic.sofanbaapp.network.model.TeamResponseData
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel

const val EXTRA_PLAYER = "EXTRA_PLAYER"
const val FAVORITE = "FAVORITE"

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenPlayer = intent?.extras?.getSerializable(EXTRA_PLAYER) as PlayersResponseData
        var isItFavorite = intent.getBooleanExtra(FAVORITE, false)

        var position = 0
        viewModel.getFavoritePlayers(this)

        playerID = chosenPlayer.id
        player = chosenPlayer

        binding.iconFavourite.setOnClickListener {
            if (isItFavorite) {
                isItFavorite = false
                binding.iconFavourite.isActivated = isItFavorite
                viewModel.removeFavoritePlayerFromDB(this, player.id)
            } else {
                isItFavorite = true
                binding.iconFavourite.isActivated = isItFavorite
                viewModel.insertFavoritePlayerToDB(this, player.toPlayerData(position))
            }
        }

        viewModel.listOfFavoritePlayers.observe(this) {
            position = it.size
        }

        setSupportActionBar(binding.toolbar)
        binding.toolbarTitle.text = PlayerHelper().getFullName(chosenPlayer)
        binding.iconFavourite.isActivated = isItFavorite
        binding.arrowBack.setOnClickListener {
            finish()
        }

        val playerActivityAdapter = PlayerActivityAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = playerActivityAdapter
        val tabs: TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
    }

    companion object {
        var playerID: Long = 0
        var player: PlayersResponseData = PlayersResponseData(
            0, "", "", "", 0, 0,
            TeamResponseData(
                0, "", "", "", "", "", ""
            ),
            0
        )
        var postseason = false
        var season = 0
    }
}
