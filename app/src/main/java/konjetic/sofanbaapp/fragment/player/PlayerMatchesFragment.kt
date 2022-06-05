package konjetic.sofanbaapp.fragment.player

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.activity.PlayerActivity.Companion.player
import konjetic.sofanbaapp.adapter.playerActivity.PlayerMatchesPagingAdapter
import konjetic.sofanbaapp.databinding.FragmentPlayerMatchesBinding
import konjetic.sofanbaapp.network.model.PlayersResponseMeta
import konjetic.sofanbaapp.network.model.TeamResponse
import konjetic.sofanbaapp.network.paging.StatsDiff
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayerMatchesFragment : Fragment() {

    val viewModel: PlayerActivityViewModel by activityViewModels()
    var _binding: FragmentPlayerMatchesBinding? = null
    val binding get() = _binding!!
    private var playoff = false
    var season = 0
    private val pagingAdapter by lazy {
        PlayerMatchesPagingAdapter(
            requireContext(), StatsDiff,
            TeamResponse(
                arrayListOf(), PlayersResponseMeta(0, 0, 0, 0, 0)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pagingAdapter.addLoadStateListener { loadState ->

            if (loadState.append.endOfPaginationReached) {
                if (pagingAdapter.itemCount < 1)
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                else
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlayerMatchesBinding.inflate(layoutInflater, container, false)

        binding.matchesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.matchesRecycler.adapter = pagingAdapter

        viewModel.getAllTeams()

        setupPlaceholder()

        viewModel.getSeasonRange(player.id)


        viewModel.seasonsRange.observe(viewLifecycleOwner) { seasons ->
            PlayerActivity.season = seasons[1]

            viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
                lifecycleScope.launch {
                    pagingAdapter.refresh()
                    viewModel.flowSeasonStats.collectLatest { stats ->
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.updateTeamsList(teams)
                        pagingAdapter.submitData(stats)
                    }
                }
            }
        }

        if (pagingAdapter.itemCount < 1) {
            binding.placeholderEmpty.placeholder.visibility = View.GONE
        } else {
            binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
        }

        pagingAdapter.addLoadStateListener { loadState ->

            if (loadState.append.endOfPaginationReached) {
                if (pagingAdapter.itemCount < 1)
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                else
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
            }
        }

        binding.headerAction.buttonRegular.isSelected = true

        binding.headerAction.buttonRegular.setOnClickListener {
            if (binding.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                PlayerActivity.postseason = playoff
            }
            if (binding.headerAction.buttonRegular.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.buttonRegular.isSelected = false
                playoff = true
                PlayerActivity.postseason = playoff
            } else {
                binding.headerAction.buttonRegular.isSelected = true
                playoff = false
                PlayerActivity.postseason = playoff
            }

            viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
                lifecycleScope.launch {
                    pagingAdapter.refresh()
                    viewModel.flowSeasonStats.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.updateTeamsList(teams)
                        pagingAdapter.submitData(it)
                    }
                }
            }
        }

        binding.headerAction.buttonPlayoffs.setOnClickListener {
            if (binding.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                PlayerActivity.postseason = playoff
            }
            if (binding.headerAction.buttonRegular.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.buttonRegular.isSelected = false
                playoff = true
                PlayerActivity.postseason = playoff
            } else {
                binding.headerAction.buttonRegular.isSelected = true
                playoff = false
                PlayerActivity.postseason = playoff
            }

            viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
                lifecycleScope.launch {
                    pagingAdapter.refresh()
                    viewModel.flowSeasonStats.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.updateTeamsList(teams)
                        pagingAdapter.submitData(it)
                    }
                }
            }
        }

        binding.headerAction.filter.setOnClickListener {
            val bottomSheetModal = PlayerSeasonsBottomSheetFragment()
            bottomSheetModal.show(parentFragmentManager, "MatchesBottomSheetDialog")
        }

        return binding.root
    }

    override fun onResume() {

        viewModel.getAllTeams()

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading")
        progressDialog.show()
        viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
            lifecycleScope.launch {
                pagingAdapter.refresh()
                viewModel.flowSeasonStats.collectLatest {
                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.updateTeamsList(teams)
                    pagingAdapter.submitData(it)
                    progressDialog.dismiss()
                }
            }
        }

        pagingAdapter.addLoadStateListener { loadState ->

            if (loadState.append.endOfPaginationReached) {
                if (pagingAdapter.itemCount < 1) {
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                } else {
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
                }
            }
        }

        setupPlaceholder()

        super.onResume()
    }

    private fun setupPlaceholder() {
        binding.placeholderEmpty.placeholderText.text = getString(R.string.placeholderFilter1)
    }
}
