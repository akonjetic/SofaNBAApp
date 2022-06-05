package konjetic.sofanbaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.adapter.ExplorePlayerPagingAdapter
import konjetic.sofanbaapp.adapter.ExploreTeamAdapter
import konjetic.sofanbaapp.adapter.PlayerSearchAdapter
import konjetic.sofanbaapp.adapter.TeamSearchAdapter
import konjetic.sofanbaapp.databinding.FragmentExploreBinding
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.network.model.PlayerImgResponseData
import konjetic.sofanbaapp.network.paging.PlayerDiff
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private var playerOrTeam = true

    private val adapterSearchPlayer by lazy { PlayerSearchAdapter(requireContext(), arrayListOf()) }
    private val adapterSearchTeam by lazy { TeamSearchAdapter(requireContext(), arrayListOf()) }
    private val adapterTeams by lazy { ExploreTeamAdapter(requireContext(), arrayListOf()) }

    private val pagingAdapter by lazy { ExplorePlayerPagingAdapter(requireContext(), PlayerDiff, viewModel) }

    private val playerImages = arrayListOf<PlayerImgResponseData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        val pagingAdapter = ExplorePlayerPagingAdapter(requireContext(), PlayerDiff, viewModel)

        viewModel.getFavoritePlayersFromDB(requireContext())
        viewModel.getAllTeams()

        binding.inputFieldSearchText.visibility = View.VISIBLE
        binding.searchResultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.playerAndTeamRecycler.layoutManager = LinearLayoutManager(requireContext())

        setupSpinner()

        viewModel.playerImages.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                playerImages.addAll(it)
                pagingAdapter.updateImgList(playerImages)
            }
        }

        return binding.root
    }

    override fun onResume() {

        viewModel.getFavoritePlayersFromDB(requireContext())
        viewModel.getAllTeams()

        viewModel.playerImages.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                playerImages.addAll(it)
                pagingAdapter.updateImgList(playerImages)
            }
        }

        super.onResume()
    }

    fun setupPlayerSearch() {

        binding.inputFieldSearchText.visibility = View.VISIBLE
        binding.inputFieldSearchTextTeam.visibility = View.GONE

        binding.searchResultsRecycler.adapter = adapterSearchPlayer

        viewModel.listOfSearchedPlayers.observe(viewLifecycleOwner) {
            adapterSearchPlayer.updateSearchList(it.data)
        }

        binding.inputFieldSearchText.doAfterTextChanged {
            viewModel.getListOfSearchedPlayers(binding.inputFieldSearchText.text.toString())
            binding.searchResultsRecycler.visibility = View.VISIBLE

            if (binding.inputFieldSearchText.text.toString() == "") {
                binding.searchResultsRecycler.visibility = View.GONE
            }
        }
    }

    fun setupSpinner() {
        val headerSelectorValues = resources.getStringArray(R.array.headerSelectorValues)
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            headerSelectorValues
        )
        binding.headerSelector.adapter = arrayAdapter

        binding.headerSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                playerOrTeam = chosenItem == headerSelectorValues[0]

                if (playerOrTeam) {
                    setupPlayerSearch()
                    binding.sectionTitle.text = getString(R.string.explore_title1)

                    binding.playerAndTeamRecycler.adapter = pagingAdapter

                    lifecycleScope.launch {
                        viewModel.flow1.collectLatest {
                            pagingAdapter.submitData(it)
                        }
                    }
                } else {
                    setupTeamSearch()
                    binding.sectionTitle.text = getString(R.string.explore_title2)
                    binding.playerAndTeamRecycler.adapter = adapterTeams

                    viewModel.listAllTeams.observe(viewLifecycleOwner) {
                        adapterTeams.updateTeamList(it.data)
                        for (team in it.data) {
                            viewModel.insertTeam(requireContext(), TeamHelper().mapToTeamInfo(team))
                        }
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }
    }

    fun setupTeamSearch() {

        binding.inputFieldSearchTextTeam.visibility = View.VISIBLE
        binding.inputFieldSearchText.visibility = View.GONE

        binding.searchResultsRecycler.adapter = adapterSearchTeam

        viewModel.listOfSearchedTeams.observe(viewLifecycleOwner) {
            adapterSearchTeam.updateSearchList(it)
        }

        binding.inputFieldSearchTextTeam.doAfterTextChanged {

            viewModel.getListOfSearchedTeams(
                requireContext(),
                binding.inputFieldSearchTextTeam.text.toString()
            )

            binding.searchResultsRecycler.visibility = View.VISIBLE

            if (binding.inputFieldSearchTextTeam.text.toString() == "") {
                binding.searchResultsRecycler.visibility = View.GONE
            }
        }
    }
}
