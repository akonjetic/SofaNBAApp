package konjetic.sofanbaapp.fragment.player

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
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.adapter.playerActivity.ImageSliderAdapter
import konjetic.sofanbaapp.adapter.playerActivity.PlayerHighlightsAdapter
import konjetic.sofanbaapp.databinding.FragmentPlayerDetailsBinding
import konjetic.sofanbaapp.helper.TeamHelper
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel

class PlayerDetailsFragment : Fragment() {

    private val viewModel: PlayerActivityViewModel by activityViewModels()
    private var _binding: FragmentPlayerDetailsBinding? = null
    private val binding get() = _binding!!
    private val highlightsAdapter by lazy { PlayerHighlightsAdapter(requireContext(), arrayListOf()) }
    private val viewPagerAdapter by lazy { ImageSliderAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlayerDetailsBinding.inflate(inflater, container, false)

        val player = PlayerActivity.player

        viewModel.getPlayerImages(player.id)
        viewModel.getHighlightsOfPlayer(player.id)

        binding.highlightsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.highlightsRecycler.adapter = highlightsAdapter

        if (viewModel.listOfPlayerImages.value == null) {
            binding.playerDetailsCard.viewpager.adapter = viewPagerAdapter
            binding.playerDetailsCard.indicator.setViewPager(binding.playerDetailsCard.viewpager)

            with(TeamHelper()) {
                binding.playerDetailsCard.teamLogo.logoBackground.editLogoColor(player.team)
                binding.playerDetailsCard.teamLogo.logoImage.editLogoImg(player.team)
            }

            binding.playerDetailsCard.teamLogo.logoImage.setOnClickListener {
                val intent = Intent(requireContext(), TeamActivity::class.java).apply {
                    putExtra(EXTRA_TEAM, player.team)
                    putExtra(FAVORITE_T, false)
                }
                requireContext().startActivity(intent)
            }

            binding.playerDetailsCard.positionName.text = when (player.position) {
                "F" -> getString(R.string.playerPosition1)
                "C" -> getString(R.string.playerPosition2)
                else -> getString(R.string.playerPosition3)
            }
            binding.playerDetailsCard.teamName.text = player.team.full_name

            if (player.height_feet == 0) {
                binding.playerDetailsCard.separatedInfo.firstDetail.value.text = getString(R.string.notApplicable)
            } else {
                binding.playerDetailsCard.separatedInfo.firstDetail.value.text = player.height_feet.toString()
            }

            if (player.weight_pounds == 0) {
                binding.playerDetailsCard.separatedInfo.secondDetail.value.text = getString(R.string.notApplicable)
            } else {
                binding.playerDetailsCard.separatedInfo.secondDetail.value.text = player.weight_pounds.toString()
            }

            binding.playerDetailsCard.separatedInfo.firstDetail.type.text = getString(R.string.playerDetail1)
            binding.playerDetailsCard.separatedInfo.secondDetail.type.text = getString(R.string.playerDetail2)
        }

        viewModel.listOfPlayerImages.observe(viewLifecycleOwner) {
            viewPagerAdapter.updateImages(it)
            binding.playerDetailsCard.viewpager.adapter = viewPagerAdapter
            binding.playerDetailsCard.indicator.setViewPager(binding.playerDetailsCard.viewpager)
        }

        with(TeamHelper()) {
            binding.playerDetailsCard.teamLogo.logoBackground.editLogoColor(player.team)
            binding.playerDetailsCard.teamLogo.logoImage.editLogoImg(player.team)
        }

        binding.playerDetailsCard.positionName.text = when (player.position) {
            "F" -> getString(R.string.playerPosition1)
            "C" -> getString(R.string.playerPosition2)
            else -> getString(R.string.playerPosition3)
        }
        binding.playerDetailsCard.teamName.text = player.team.full_name

        if (player.height_feet == 0) {
            binding.playerDetailsCard.separatedInfo.firstDetail.value.text = getString(R.string.notApplicable)
        } else {
            binding.playerDetailsCard.separatedInfo.firstDetail.value.text = player.height_feet.toString()
        }

        if (player.weight_pounds == 0) {
            binding.playerDetailsCard.separatedInfo.secondDetail.value.text = getString(R.string.notApplicable)
        } else {
            binding.playerDetailsCard.separatedInfo.secondDetail.value.text = player.weight_pounds.toString()
        }

        binding.playerDetailsCard.separatedInfo.firstDetail.type.text = getString(R.string.playerDetail1)
        binding.playerDetailsCard.separatedInfo.secondDetail.type.text = getString(R.string.playerDetail2)

        val bottomSheetModal = PlayerPhotoBottomSheetFragment(binding)

        binding.playerDetailsCard.addPhoto.setOnClickListener {
            bottomSheetModal.show(parentFragmentManager, "BottomSheetDialog")
        }

        viewModel.playerHighlights.observe(viewLifecycleOwner) {
            highlightsAdapter.updateHighlights(it)
        }

        return binding.root
    }

    override fun onResume() {
        val player = PlayerActivity.player

        viewModel.getPlayerImages(player.id)
        viewModel.getHighlightsOfPlayer(player.id)

        viewModel.listOfPlayerImages.observe(viewLifecycleOwner) {
            viewPagerAdapter.updateImages(it)
            viewPagerAdapter.notifyDataSetChanged()
            binding.playerDetailsCard.viewpager.adapter = viewPagerAdapter
            binding.playerDetailsCard.indicator.setViewPager(binding.playerDetailsCard.viewpager)
        }

        viewModel.playerHighlights.observe(viewLifecycleOwner) {
            highlightsAdapter.updateHighlights(it)
        }

        super.onResume()
    }
}
