package konjetic.sofanbaapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.adapter.matchActivity.MatchActivityAdapter
import konjetic.sofanbaapp.databinding.ActivityMatchBinding
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.network.model.TeamResponseData

const val EXTRA_MATCH = "EXTRA_MATCH"

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenMatch = intent?.extras?.getSerializable(EXTRA_MATCH) as MatchResponseData

        match = chosenMatch

        setSupportActionBar(binding.toolbar)
        binding.toolbarSubtitle.text = getString(R.string.match_teams_template, match.home_team.full_name, match.visitor_team.full_name).uppercase()
        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.toolbar.setBackgroundColor(getColor(R.color.color_primary))

        val matchActivityAdapter = MatchActivityAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = matchActivityAdapter
        val tabs: TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
    }

    companion object {
        var match = MatchResponseData(0, "", TeamResponseData(0, "", "", "", "", "", ""), 0, 0, false, 0, "", "", TeamResponseData(0, "", "", "", "", "", ""), 0)
    }
}
