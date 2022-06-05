package konjetic.sofanbaapp.fragment.match

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.EXTRA_TEAM
import konjetic.sofanbaapp.activity.FAVORITE_T
import konjetic.sofanbaapp.activity.MatchActivity
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.adapter.matchActivity.MatchHighlightsAdapter
import konjetic.sofanbaapp.databinding.FragmentMatchDetailsBinding
import konjetic.sofanbaapp.helper.DateTimeHelper
import konjetic.sofanbaapp.helper.MatchHelper
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.viewmodel.MatchActivityViewModel

class MatchDetailsFragment : Fragment() {

    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MatchActivityViewModel by activityViewModels()
    private val highlightsAdapter by lazy { MatchHighlightsAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMatchDetailsBinding.inflate(layoutInflater, container, false)

        val game = MatchActivity.match

        viewModel.getGameStats(game.id)
        viewModel.getMatchHighlights(game.id)

        binding.highlightsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.highlightsRecycler.adapter = highlightsAdapter

        with(TeamHelper()) {
            binding.matchStats.matchResItem.firstTeam.logoBackground.editLogoColor(game.home_team)
            binding.matchStats.matchResItem.secondTeam.logoBackground.editLogoColor(game.visitor_team)
            binding.matchStats.matchResItem.firstTeam.logoImage.editLogoImg(game.home_team)
            binding.matchStats.matchResItem.secondTeam.logoImage.editLogoImg(game.visitor_team)
        }

        binding.matchStats.matchResItem.date.text = DateTimeHelper().getDateLongFormat(game.date)
        binding.matchStats.matchResItem.firstTeamScore.text = game.home_team_score.toString()
        binding.matchStats.matchResItem.secondTeamScore.text = game.visitor_team_score.toString()
        binding.matchStats.matchResItem.finalLabel.text = game.status

        viewModel.eventHighlights.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.headerLayout.visibility = View.GONE
                binding.highlightsPlaceholder.highlightsPlaceholder.visibility = View.VISIBLE
                binding.highlightsRecycler.visibility = View.GONE
            } else {
                binding.highlightsPlaceholder.highlightsPlaceholder.visibility = View.GONE
                highlightsAdapter.updateHighlights(it)
                binding.highlightsRecycler.visibility = View.VISIBLE
                binding.headerLayout.visibility = View.VISIBLE
            }
        }

        viewModel.gameStats.observe(viewLifecycleOwner) {

            val (teamHome, teamVisitor) = MatchHelper().getTeamStatsSeparated(it)

            val teamHomeColor = TeamHelper().getTeamColorNameOpacity(teamHome[0].team.name, requireContext())
            val teamVisitorColor = TeamHelper().getTeamColorNameOpacity(teamVisitor[0].team.name, requireContext())

            binding.matchStats.matchResItem.firstTeamName.text = teamHome[0].team.abbreviation.uppercase()
            binding.matchStats.matchResItem.secondTeamName.text = teamVisitor[0].team.abbreviation.uppercase()

            val (fgPctH, fgPctV) = MatchHelper().getPercentFg(teamHome, teamVisitor)
            binding.matchStats.fgPct.firstTeamBar.setProgress(fgPctH, false)
            binding.matchStats.fgPct.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.fgPct.firstTeamValue.text = fgPctH.toString()
            binding.matchStats.fgPct.secondTeamBar.setProgress(fgPctV, false)
            binding.matchStats.fgPct.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.fgPct.secondTeamValue.text = fgPctV.toString()
            binding.matchStats.fgPct.statName.text = getString(R.string.matchStatTitle1)

            val (fg3PctH, fg3PctV) = MatchHelper().getPercentFg3(teamHome, teamVisitor)
            binding.matchStats.fg3Pct.firstTeamBar.setProgress(fg3PctH, false)
            binding.matchStats.fg3Pct.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.fg3Pct.firstTeamValue.text = fg3PctH.toString()
            binding.matchStats.fg3Pct.secondTeamBar.setProgress(fg3PctV, false)
            binding.matchStats.fg3Pct.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.fg3Pct.secondTeamValue.text = fg3PctV.toString()
            binding.matchStats.fg3Pct.statName.text = getString(R.string.matchStatTitle2)

            val (rebH, rebV) = MatchHelper().getReb(teamHome, teamVisitor)
            binding.matchStats.reb.firstTeamBar.max = rebH + rebV
            binding.matchStats.reb.secondTeamBar.max = rebH + rebV
            binding.matchStats.reb.firstTeamBar.setProgress(rebH, false)
            binding.matchStats.reb.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.reb.secondTeamBar.setProgress(rebV, false)
            binding.matchStats.reb.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.reb.firstTeamValue.text = rebH.toString()
            binding.matchStats.reb.secondTeamValue.text = rebV.toString()
            binding.matchStats.reb.statName.text = getString(R.string.matchStatTitle3)

            val (astH, astV) = MatchHelper().getAst(teamHome, teamVisitor)
            binding.matchStats.ast.firstTeamBar.max = astH + astV
            binding.matchStats.ast.secondTeamBar.max = astH + astV
            binding.matchStats.ast.firstTeamBar.setProgress(astH, false)
            binding.matchStats.ast.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.ast.secondTeamBar.setProgress(astV, false)
            binding.matchStats.ast.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.ast.firstTeamValue.text = astH.toString()
            binding.matchStats.ast.secondTeamValue.text = astV.toString()
            binding.matchStats.ast.statName.text = getString(R.string.matchStatTitle4)

            val (tovH, tovV) = MatchHelper().getTov(teamHome, teamVisitor)
            binding.matchStats.tov.firstTeamBar.max = tovH + tovV
            binding.matchStats.tov.secondTeamBar.max = tovH + tovV
            binding.matchStats.tov.firstTeamBar.setProgress(tovH, false)
            binding.matchStats.tov.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.tov.secondTeamBar.setProgress(tovV, false)
            binding.matchStats.tov.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.tov.firstTeamValue.text = tovH.toString()
            binding.matchStats.tov.secondTeamValue.text = tovV.toString()
            binding.matchStats.tov.statName.text = getString(R.string.matchStatTitle5)

            val (orebH, orebV) = MatchHelper().getOreb(teamHome, teamVisitor)
            binding.matchStats.oreb.firstTeamBar.max = orebH + orebV
            binding.matchStats.oreb.secondTeamBar.max = orebH + orebV
            binding.matchStats.oreb.firstTeamBar.setProgress(orebH, false)
            binding.matchStats.oreb.firstTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamHomeColor, "color", requireContext().packageName))
            binding.matchStats.oreb.secondTeamBar.setProgress(orebV, false)
            binding.matchStats.oreb.secondTeamBar.progressTintList = requireContext().getColorStateList(resources.getIdentifier(teamVisitorColor, "color", requireContext().packageName))
            binding.matchStats.oreb.firstTeamValue.text = orebH.toString()
            binding.matchStats.oreb.secondTeamValue.text = orebV.toString()
            binding.matchStats.oreb.statName.text = getString(R.string.matchStatTitle6)

            binding.matchStats.matchResItem.firstTeam.logoImage.setOnClickListener {
                val intent = Intent(requireContext(), TeamActivity::class.java).apply {
                    putExtra(EXTRA_TEAM, game.home_team)
                    putExtra(FAVORITE_T, false)
                }
                requireContext().startActivity(intent)
            }

            binding.matchStats.matchResItem.secondTeam.logoImage.setOnClickListener {
                val intent = Intent(requireContext(), TeamActivity::class.java).apply {
                    putExtra(EXTRA_TEAM, game.visitor_team)
                    putExtra(FAVORITE_T, false)
                }
                requireContext().startActivity(intent)
            }

            val bottomSheetModal = MatchHighlightBottomSheetFragment()
            binding.highlightsPlaceholder.addUrlButton.setOnClickListener {
                bottomSheetModal.show(parentFragmentManager, "BottomSheetDialog")
            }

            binding.linkIcon.setOnClickListener {
                bottomSheetModal.show(parentFragmentManager, "BottomSheetDialog")
            }
        }

        return binding.root
    }
}
