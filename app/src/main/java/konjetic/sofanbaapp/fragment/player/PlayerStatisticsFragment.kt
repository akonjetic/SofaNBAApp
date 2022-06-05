package konjetic.sofanbaapp.fragment.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.adapter.playerActivity.PlayerStatsAdapter
import konjetic.sofanbaapp.databinding.FragmentPlayerStatisticsBinding
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel

class PlayerStatisticsFragment : Fragment() {

    var _binding: FragmentPlayerStatisticsBinding? = null
    val binding get() = _binding!!
    val viewModel: PlayerActivityViewModel by viewModels()

    private val playerStatsAdapter by lazy { PlayerStatsAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerStatisticsBinding.inflate(layoutInflater, container, false)

        viewModel.getSeasonRange(PlayerActivity.player.id)

        binding.statsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.statsRecycler.adapter = playerStatsAdapter

        viewModel.seasonsRange.observe(viewLifecycleOwner) {
            val arrayOfSeasons: ArrayList<Int> = arrayListOf()

            arrayOfSeasons.add(it[1])
            arrayOfSeasons.add(it[1] - 1)
            arrayOfSeasons.add(it[1] - 2)
            arrayOfSeasons.add(it[1] - 3)

            viewModel.getSeasonAverage(PlayerActivity.player.id, arrayOfSeasons)
        }

        viewModel.seasonAverages.observe(viewLifecycleOwner) {

            val seasonStatsTemp = resources.getStringArray(R.array.playerStatsValues)
            val seasonsStats: ArrayList<String> = seasonStatsTemp.toCollection(ArrayList())

            if (it.size > 0) {
                if (it[0].data.size > 0) {
                    binding.seasonsListHeader.seasonOne.text = it[0].data[0].season.toStringOrEmpty()
                    if (it[0].data[0].season.toString() != "") {
                        binding.seasonsListHeader.seasonOne.text = it[0].data[0].season.toString()
                        seasonsStats.add(it[0].data[0].min)
                        seasonsStats.add(it[0].data[0].fgm.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fga.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fg3m.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fg3a.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].ftm.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fta.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].oreb.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].dreb.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].reb.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].ast.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].stl.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].blk.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].turnover.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].pf.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].pts.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fg_pct.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].fg3_pct.toStringOrEmpty())
                        seasonsStats.add(it[0].data[0].ft_pct.toStringOrEmpty())
                    }
                } else {
                    for (i in 0..18) {
                        seasonsStats.add("N/A")
                    }
                }
            } else {
                binding.seasonsListHeader.seasonOne.text = " "
                for (i in 0..18) {
                    seasonsStats.add("")
                }
            }

            if (it.size > 1) {
                if (it[1].data.size > 0) {
                    binding.seasonsListHeader.seasonTwo.text = it[1].data[0].season.toStringOrEmpty()

                    if (it[1].data[0].season.toString() != "") {
                        binding.seasonsListHeader.seasonTwo.text = it[1].data[0].season.toString()
                        seasonsStats.add(it[1].data[0].min)
                        seasonsStats.add(it[1].data[0].fgm.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fga.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fg3m.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fg3a.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].ftm.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fta.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].oreb.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].dreb.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].reb.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].ast.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].stl.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].blk.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].turnover.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].pf.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].pts.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fg_pct.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].fg3_pct.toStringOrEmpty())
                        seasonsStats.add(it[1].data[0].ft_pct.toStringOrEmpty())
                    }
                } else {
                    for (i in 0..18) {
                        seasonsStats.add("N/A")
                    }
                }
            } else {
                binding.seasonsListHeader.seasonTwo.text = " "
                for (i in 0..18) {
                    seasonsStats.add("")
                }
            }

            if (it.size > 2) {
                if (it[2].data.size > 0) {
                    binding.seasonsListHeader.seasonThree.text = it[2].data[0].season.toStringOrEmpty()

                    if (it[2].data[0].season.toString() != "") {
                        binding.seasonsListHeader.seasonThree.text = it[2].data[0].season.toString()
                        seasonsStats.add(it[2].data[0].min)
                        seasonsStats.add(it[2].data[0].fgm.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fga.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fg3m.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fg3a.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].ftm.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fta.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].oreb.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].dreb.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].reb.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].ast.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].stl.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].blk.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].turnover.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].pf.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].pts.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fg_pct.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].fg3_pct.toStringOrEmpty())
                        seasonsStats.add(it[2].data[0].ft_pct.toStringOrEmpty())
                    }
                } else {
                    for (i in 0..18) {
                        seasonsStats.add("N/A")
                    }
                }
            } else {
                binding.seasonsListHeader.seasonThree.text = " "
                for (i in 0..18) {
                    seasonsStats.add("")
                }
            }

            if (it.size > 3) {
                if (it[3].data.size > 0) {
                    binding.seasonsListHeader.seasonFour.text = it[3].data[0].season.toStringOrEmpty()

                    if (it[3].data[0].season.toString() != "") {
                        binding.seasonsListHeader.seasonFour.text = it[3].data[0].season.toString()
                        seasonsStats.add(it[3].data[0].min)
                        seasonsStats.add(it[3].data[0].fgm.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fga.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fg3m.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fg3a.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].ftm.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fta.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].oreb.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].dreb.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].reb.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].ast.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].stl.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].blk.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].turnover.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].pf.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].pts.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fg_pct.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].fg3_pct.toStringOrEmpty())
                        seasonsStats.add(it[3].data[0].ft_pct.toStringOrEmpty())
                    }
                } else {
                    for (i in 0..18) {
                        seasonsStats.add("N/A")
                    }
                }
            } else {
                binding.seasonsListHeader.seasonFour.text = " "
                for (i in 0..18) {
                    seasonsStats.add("")
                }
            }

            playerStatsAdapter.updateData(seasonsStats)
        }

        return binding.root
    }

    private fun Any?.toStringOrEmpty() = this?.toString() ?: ""
}
