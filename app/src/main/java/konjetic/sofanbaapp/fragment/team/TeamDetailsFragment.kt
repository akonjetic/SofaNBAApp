package konjetic.sofanbaapp.fragment.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.adapter.teamActivity.TeamDivisionAdapter
import konjetic.sofanbaapp.database.entity.TeamInfo
import konjetic.sofanbaapp.databinding.FragmentTeamDetailsBinding
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.viewmodel.TeamActivityViewModel

class TeamDetailsFragment : Fragment() {

    var _binding: FragmentTeamDetailsBinding? = null
    val binding get() = _binding!!
    private val viewModel: TeamActivityViewModel by activityViewModels()
    private val divisionAdapter by lazy { TeamDivisionAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamDetailsBinding.inflate(layoutInflater, container, false)
        val team = TeamActivity.team

        binding.teamDivisonRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.teamDivisonRecycler.adapter = divisionAdapter

        with(TeamHelper()) {
            binding.infoCard.teamLogo.logoBackground.editLogoColor(team)
            binding.infoCard.teamLogo.logoImage.editLogoImg(team)
        }

        binding.infoCard.teamAbbrev.text = team.abbreviation.uppercase()
        binding.infoCard.teamFullName.text = team.full_name
        binding.infoCard.cityName.text = team.city.uppercase()
        binding.bottomInfo.firstDetail.value.text = team.conference
        binding.bottomInfo.firstDetail.type.text = getString(R.string.playerDetailTitle1)
        binding.bottomInfo.secondDetail.value.text = team.division
        binding.bottomInfo.secondDetail.type.text = getString(R.string.playerDetailTitle2)

        viewModel.getTeamsOfDivison(requireContext(), team.division)

        viewModel.teamsOfDivision.observe(viewLifecycleOwner) {
            divisionAdapter.updateTeamsList(it.filter { t -> t.id != team.id } as ArrayList<TeamInfo>)
        }

        return binding.root
    }
}
