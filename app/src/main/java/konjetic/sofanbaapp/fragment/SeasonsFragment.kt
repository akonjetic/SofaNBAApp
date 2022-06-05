package konjetic.sofanbaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MainActivity
import konjetic.sofanbaapp.adapter.MatchPagingAdapter
import konjetic.sofanbaapp.databinding.FragmentSeasonsBinding
import konjetic.sofanbaapp.network.paging.MatchDiff
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SeasonsFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!

    private val pagingAdapter by lazy { MatchPagingAdapter(requireContext(), MatchDiff) }

    private var playoff = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)

        binding.seasonsRecycler.layoutManager = LinearLayoutManager(requireContext())

        setupPlaceholder()

        if (MainActivity.season == 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
        } else if (MainActivity.season == 0 && MainActivity.teamId != 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.GONE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
        } else if (MainActivity.season > 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.GONE
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = MainActivity.season.toString()
        } else {
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = MainActivity.season.toString()
        }

        MainActivity.playoff = playoff
        binding.headerAction.headerAction.buttonRegular.isSelected = true

        binding.headerAction.headerAction.buttonRegular.setOnClickListener {
            if (binding.headerAction.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                MainActivity.playoff = playoff
            }
            if (binding.headerAction.headerAction.buttonRegular.isSelected) {
                binding.headerAction.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.headerAction.buttonRegular.isSelected = false
                playoff = true
                MainActivity.playoff = playoff
            } else {
                binding.headerAction.headerAction.buttonRegular.isSelected = true
                playoff = false
                MainActivity.playoff = playoff
            }

            lifecycleScope.launch {
                pagingAdapter.refresh()
                viewModel.flowSeasonMatches.collectLatest {

                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.submitData(it)
                }
            }
        }

        binding.headerAction.headerAction.buttonPlayoffs.setOnClickListener {
            if (binding.headerAction.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                MainActivity.playoff = playoff
            }
            if (binding.headerAction.headerAction.buttonRegular.isSelected) {
                binding.headerAction.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.headerAction.buttonRegular.isSelected = false
                playoff = true
                MainActivity.playoff = playoff
            } else {
                binding.headerAction.headerAction.buttonRegular.isSelected = true
                playoff = false
                MainActivity.playoff = playoff
            }

            lifecycleScope.launch {
                pagingAdapter.refresh()
                viewModel.flowSeasonMatches.collectLatest {

                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.submitData(it)
                }
            }
        }

        binding.headerAction.filteredHeader.seasonsFilterDelete.setOnClickListener {
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.GONE
            MainActivity.season = 0

            if (MainActivity.teamId == 0L) {
                binding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
            }

            lifecycleScope.launch {
                pagingAdapter.refresh()
                viewModel.flowSeasonMatches.collectLatest {
                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.submitData(it)
                }
            }
        }

        binding.headerAction.filteredHeader.teamsFilterDelete.setOnClickListener {
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.GONE
            MainActivity.teamId = 0L
            MainActivity.teamName = ""

            if (MainActivity.season == 0) {
                binding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
            }

            lifecycleScope.launch {

                pagingAdapter.refresh()
                viewModel.flowSeasonMatches.collectLatest {
                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.submitData(it)
                }
            }
        }

        binding.seasonsRecycler.adapter = pagingAdapter

        lifecycleScope.launch {
            pagingAdapter.refresh()
            viewModel.flowSeasonMatches.collectLatest {
                pagingAdapter.submitData(PagingData.empty())
                pagingAdapter.submitData(it)
            }
        }

        val bottomSheetModal = SeasonFilterBottomSheetFragment(pagingAdapter, binding)

        binding.headerAction.headerAction.filter.setOnClickListener {
            bottomSheetModal.show(parentFragmentManager, "BottomSheetDialog")
        }

        if (pagingAdapter.itemCount < 1) {
            binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
        } else {
            binding.placeholderEmpty.placeholder.visibility = View.GONE
        }

        pagingAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading) {
                if (pagingAdapter.itemCount < 1) {
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                } else {
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
                }
            }

            if (loadState.append.endOfPaginationReached) {
                if (pagingAdapter.itemCount < 1)
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                else
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onResume() {
        if (MainActivity.season == 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
        } else if (MainActivity.season == 0 && MainActivity.teamId != 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.GONE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
        } else if (MainActivity.season > 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.GONE
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = getString(R.string.filter_season_template, MainActivity.season, MainActivity.season + 1)
        } else {
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = getString(R.string.filter_season_template, MainActivity.season, MainActivity.season + 1)
        }

        lifecycleScope.launch {
            pagingAdapter.refresh()
            viewModel.flowSeasonMatches.collectLatest {

                pagingAdapter.submitData(PagingData.empty())
                pagingAdapter.submitData(it)
            }
        }

        if (MainActivity.season == 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
        } else if (MainActivity.season == 0 && MainActivity.teamId != 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.GONE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
        } else if (MainActivity.season > 0 && MainActivity.teamId == 0L) {
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.GONE
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = MainActivity.season.toString()
        } else {
            binding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
            binding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
            binding.headerAction.filteredHeader.seasonsFilterValueText.text = MainActivity.season.toString()
        }

        if (pagingAdapter.itemCount < 1) {
            binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
        } else {
            binding.placeholderEmpty.placeholder.visibility = View.GONE
        }

        pagingAdapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading) {
                if (pagingAdapter.itemCount < 1) {
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                } else {
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
                }
            }

            if (loadState.append.endOfPaginationReached) {
                if (pagingAdapter.itemCount < 1)
                    binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                else
                    binding.placeholderEmpty.placeholder.visibility = View.GONE
            }
        }

        setupPlaceholder()

        super.onResume()
    }

    fun setupPlaceholder() {
        binding.placeholderEmpty.placeholderText.text = getString(R.string.placeholderFilter1)
    }
}
