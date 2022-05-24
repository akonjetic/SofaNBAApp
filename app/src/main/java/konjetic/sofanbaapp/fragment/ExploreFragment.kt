package konjetic.sofanbaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MainActivityViewModel
import konjetic.sofanbaapp.adapter.PlayerPagingAdapter
import konjetic.sofanbaapp.adapter.PlayerSearchAdapter
import konjetic.sofanbaapp.adapter.TeamAdapter
import konjetic.sofanbaapp.databinding.FragmentExploreBinding
import konjetic.sofanbaapp.network.paging.PlayerDiff
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private var playerOrTeam = true

    private val adapterSearchPlayer by lazy { PlayerSearchAdapter(requireContext(), arrayListOf()) }
    private val adapterTeams by lazy { TeamAdapter(requireContext(), arrayListOf()) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)


        viewModel.getFavoritePlayersFromDB(requireContext())
        viewModel.getAllTeams()


        ///////////SEARCH
        binding.searchResultsRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.playerAndTeamRecycler.layoutManager = LinearLayoutManager(requireContext())


        //IGRAČI
        if (playerOrTeam) {
            binding.searchResultsRecycler.adapter = adapterSearchPlayer

            viewModel.listOfSearchedPlayers.observe(viewLifecycleOwner) {
                adapterSearchPlayer.updateSearchList(it.data)
            }

            //pretrazivanje po imenu, network poziv
            binding.inputFieldSearchText.doAfterTextChanged {
                viewModel.getListOfSearchedPlayers(binding.inputFieldSearchText.text.toString())
                binding.searchResultsRecycler.visibility = View.VISIBLE

                if (binding.inputFieldSearchText.text.toString() == "") {
                    binding.searchResultsRecycler.visibility = View.GONE
                }
            }
        } else{
            //TIMOVI
            //mozda iz baze jer nema na APIu?
        }



        ////////////////SPINNER
        //popunjavanje spinnera
        val headerSelectorValues = resources.getStringArray(R.array.headerSelectorValues)

        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            headerSelectorValues
            )

        binding.headerSelector.adapter = arrayAdapter

        //promjena vrijednosti u spinneru
        binding.headerSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val chosenItem = p0?.getItemAtPosition(p2).toString()
                playerOrTeam = chosenItem == headerSelectorValues[0]


                //////////////TEAM & PLAYER RECYCLER


                if(playerOrTeam) {
                    binding.sectionTitle.text = getString(R.string.explore_title1)
                    val pagingAdapter = PlayerPagingAdapter(requireContext(), PlayerDiff)
                    binding.playerAndTeamRecycler.adapter = pagingAdapter


                    // radilo je ali vise ne radi hahahhahaha ?!?!?!?
                    lifecycleScope.launch {
                        viewModel.flow1.collectLatest {
                            pagingAdapter.submitData(it)
                            pagingAdapter.submitData(lifecycle, PagingData.empty())
                        }
                    }
                    viewModel.playerImages.observe(viewLifecycleOwner){

                        pagingAdapter.updateImgList(it)
                    }
                } else{
                    binding.sectionTitle.text = getString(R.string.explore_title2)
                    binding.playerAndTeamRecycler.adapter = adapterTeams

                    viewModel.listAllTeams.observe(viewLifecycleOwner){
                        adapterTeams.updateTeamList(it.data)
                    }
                }
        }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                /**/
            }
        }


        return binding.root
    }




}