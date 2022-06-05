package konjetic.sofanbaapp.fragment.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.databinding.BottomSheetLayoutPlayerMatcherBinding
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel

class PlayerSeasonsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutPlayerMatcherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetLayoutPlayerMatcherBinding.inflate(inflater, container, false)

        setupBottomSheet()

        val player = PlayerActivity.player
        var firstSeason: Int
        var lastSeason: Int

        viewModel.getSeasonRange(player.id)
        viewModel.seasonsRange.observe(viewLifecycleOwner) {
            firstSeason = it[0]
            lastSeason = it[1]

            var season = lastSeason
            val seasonsRange: ArrayList<String> = arrayListOf()
            seasonsRange.add(getString(R.string.matches_spinner_placeholder))
            while (season >= firstSeason) {
                seasonsRange.add(season.toString())
                season--
            }

            val arrayAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                seasonsRange
            )

            binding.seasonSpinner.adapter = arrayAdapter
        }

        binding.actionButtons.addButton.setOnClickListener {
            if (binding.seasonSpinner.selectedItem.toString() != getString(R.string.matches_spinner_placeholder)) {
                PlayerActivity.season = binding.seasonSpinner.selectedItem.toString().toInt()
                this.dismiss()
            }
        }

        binding.actionButtons.cancelButton.setOnClickListener {
            this.dismiss()
        }

        return binding.root
    }

    private fun setupBottomSheet() {
        binding.sheetHeader.headerTitle.text = getString(R.string.bottomSheetPlayerSeasonTitle)
        binding.actionButtons.addButton.text = getString(R.string.bottomSheetPlayerSeasonButton)
    }
}
