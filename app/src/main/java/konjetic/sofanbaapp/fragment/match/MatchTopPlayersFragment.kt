package konjetic.sofanbaapp.fragment.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MatchActivity
import konjetic.sofanbaapp.adapter.matchActivity.MatchTopPlayerAdapter
import konjetic.sofanbaapp.databinding.FragmentMatchTopPlayersBinding
import konjetic.sofanbaapp.viewmodel.MatchActivityViewModel

class MatchTopPlayersFragment : Fragment() {

    private val viewModel: MatchActivityViewModel by activityViewModels()
    private var _binding: FragmentMatchTopPlayersBinding? = null
    private val binding get() = _binding!!

    private val topPlayersAdapter1 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }
    private val topPlayersAdapter2 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }
    private val topPlayersAdapter3 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }
    private val topPlayersAdapter4 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }
    private val topPlayersAdapter5 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }
    private val topPlayersAdapter6 by lazy { MatchTopPlayerAdapter(requireContext(), arrayListOf(), "") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchTopPlayersBinding.inflate(layoutInflater, container, false)

        val match = MatchActivity.match
        viewModel.getGameStats(match.id)

        binding.fieldGoals.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fieldGoals.topPlayerRecycler.adapter = topPlayersAdapter1

        binding.threePointers.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.threePointers.topPlayerRecycler.adapter = topPlayersAdapter2

        binding.rebounds.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.rebounds.topPlayerRecycler.adapter = topPlayersAdapter3

        binding.offRebounds.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.offRebounds.topPlayerRecycler.adapter = topPlayersAdapter4

        binding.assists.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.assists.topPlayerRecycler.adapter = topPlayersAdapter5

        binding.turnovers.topPlayerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.turnovers.topPlayerRecycler.adapter = topPlayersAdapter6

        viewModel.gameStats.observe(viewLifecycleOwner) {

            for (player in it.data) {
                viewModel.getPlayerImages(player.player.id)
            }

            topPlayersAdapter1.updateCategory(getString(R.string.matchStatTitle1))
            it.data.sortByDescending { p -> p.fgm }
            topPlayersAdapter1.updatePlayersList(it.data)

            topPlayersAdapter2.updateCategory(getString(R.string.matchStatTitle2))
            it.data.sortByDescending { p -> p.fg3m }
            topPlayersAdapter2.updatePlayersList(it.data)

            topPlayersAdapter3.updateCategory(getString(R.string.matchStatTitle3))
            it.data.sortByDescending { p -> p.reb }
            topPlayersAdapter3.updatePlayersList(it.data)

            topPlayersAdapter4.updateCategory(getString(R.string.matchStatTitle6))
            it.data.sortByDescending { p -> p.oreb }
            topPlayersAdapter4.updatePlayersList(it.data)

            topPlayersAdapter5.updateCategory(getString(R.string.matchStatTitle4))
            it.data.sortByDescending { p -> p.ast }
            topPlayersAdapter5.updatePlayersList(it.data)

            topPlayersAdapter6.updateCategory(getString(R.string.matchStatTitle5))
            it.data.sortByDescending { p -> p.turnover }
            topPlayersAdapter6.updatePlayersList(it.data)
        }

        binding.fieldGoals.statsName.text = getString(R.string.topPlayersStatTitle1)
        binding.threePointers.statsName.text = getString(R.string.topPlayersStatTitle2)
        binding.offRebounds.statsName.text = getString(R.string.topPlayersStatTitle3)
        binding.rebounds.statsName.text = getString(R.string.topPlayersStatTitle4)
        binding.assists.statsName.text = getString(R.string.topPlayersStatTitle5)
        binding.turnovers.statsName.text = getString(R.string.topPlayersStatTitle6)

        viewModel.playerImages.observe(viewLifecycleOwner) {
            topPlayersAdapter1.updateImagesList(it)
            topPlayersAdapter2.updateImagesList(it)
            topPlayersAdapter3.updateImagesList(it)
            topPlayersAdapter4.updateImagesList(it)
            topPlayersAdapter5.updateImagesList(it)
            topPlayersAdapter6.updateImagesList(it)
        }

        return binding.root
    }
}
