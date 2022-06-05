package konjetic.sofanbaapp.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MainActivity
import konjetic.sofanbaapp.adapter.MatchPagingAdapter
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.databinding.BottomSheetLayoutSeasonsFragmentBinding
import konjetic.sofanbaapp.databinding.FragmentSeasonsBinding
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SeasonFilterBottomSheetFragment(private val pagingAdapter: MatchPagingAdapter, private val seasonsBinding: FragmentSeasonsBinding) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutSeasonsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by viewModels()

    private var teamsList = arrayListOf<TeamInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetLayoutSeasonsFragmentBinding.inflate(inflater, container, false)

        viewModel.getAllTeamsFromDB(requireContext())
        setupDialog()

        viewModel.allTeamsFromDB.observe(viewLifecycleOwner) {
            teamsList.clear()
            teamsList.addAll(it)
            setupSpinnerTeams()
            setupSpinnerSeasons()
        }

        binding.bottomButtons.addButton.setOnClickListener {
            var team: Long? = null
            var season: Int? = null
            if (binding.teamSpinner.selectedItem.toString() != getString(R.string.seasonsFilterPlaceholder1)) {
                val teamName = binding.teamSpinner.selectedItem.toString()
                MainActivity.teamName = teamName
                for (item in teamsList) {
                    if (item.full_name == teamName) {
                        team = item.id
                        break
                    }
                }
            }
            if (binding.seasonSpinner.selectedItem.toString() != getString(R.string.seasonsFilterPlaceholder2)) {
                season = binding.seasonSpinner.selectedItem.toString().toInt()
            }

            MainActivity.season = season ?: 0
            MainActivity.teamId = team ?: 0

            lifecycleScope.launch {
                pagingAdapter.refresh()
                viewModel.flowSeasonMatches.collectLatest {

                    pagingAdapter.submitData(PagingData.empty())
                    pagingAdapter.submitData(it)
                }
            }

            if (MainActivity.season == 0 && MainActivity.teamId == 0L) {
                seasonsBinding.headerAction.filteredHeader.filterLayout.visibility = View.GONE
            } else if (MainActivity.season == 0 && MainActivity.teamId != 0L) {
                seasonsBinding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.seasonFilterCard.visibility = View.GONE
                seasonsBinding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
            } else if (MainActivity.season > 0 && MainActivity.teamId == 0L) {
                seasonsBinding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.teamFilterValue.visibility = View.GONE
                seasonsBinding.headerAction.filteredHeader.seasonsFilterValueText.text = getString(R.string.filter_season_template, MainActivity.season, MainActivity.season + 1)
            } else {
                seasonsBinding.headerAction.filteredHeader.seasonFilterCard.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.teamFilterValue.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.filterLayout.visibility = View.VISIBLE
                seasonsBinding.headerAction.filteredHeader.teamsFilterValueText.text = MainActivity.teamName
                seasonsBinding.headerAction.filteredHeader.seasonsFilterValueText.text = getString(R.string.filter_season_template, MainActivity.season, MainActivity.season + 1)
            }

            Handler().postDelayed({
                this.dismiss()
            }, 1000)
        }

        binding.bottomButtons.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        viewModel.getAllTeamsFromDB(requireContext())
        super.onResume()
    }

    fun setupDialog(){
        binding.header.headerTitle.text = getString(R.string.bottomSheetPlayerSeasonTitle)
        binding.bottomButtons.addButton.text = getString(R.string.bottomSheetPlayerSeasonButton)
    }

    private fun setupSpinnerSeasons() {
        val seasonsSpinnerList = arrayListOf<String>()
        seasonsSpinnerList.add(getString(R.string.seasonsFilterPlaceholder2))

        for (i in 2021 downTo 1978) {
            seasonsSpinnerList.add(i.toString())
        }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            seasonsSpinnerList
        )
        binding.seasonSpinner.adapter = arrayAdapter
    }

    private fun setupSpinnerTeams() {
        val teamsSpinnerList = arrayListOf<String>()
        teamsSpinnerList.add(getString(R.string.seasonsFilterPlaceholder1))

        for (item in teamsList) {
            teamsSpinnerList.add(item.full_name)
        }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            teamsSpinnerList
        )
        binding.teamSpinner.adapter = arrayAdapter
    }
}
