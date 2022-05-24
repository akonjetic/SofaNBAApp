package konjetic.sofanbaapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import konjetic.sofanbaapp.databinding.ActivityPlayerBinding
import konjetic.sofanbaapp.helper.PlayerHelper
import konjetic.sofanbaapp.network.model.PlayersResponseData

const val EXTRA_PLAYER = "EXTRA_PLAYER"
const val FAVORITE = "FAVORITE"

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenPlayer = intent?.extras?.getSerializable(EXTRA_PLAYER) as PlayersResponseData
        var isItFavorite = intent.getBooleanExtra(FAVORITE, false)

        binding.toolbarTitle.text = PlayerHelper().getFullName(chosenPlayer)
        binding.iconFavourite.isActivated = isItFavorite


    }
}