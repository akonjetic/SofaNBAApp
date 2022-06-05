package konjetic.sofanbaapp.fragment.player

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import konjetic.sofanbaapp.activity.PlayerActivity
import konjetic.sofanbaapp.databinding.BottomSheetLayoutBinding
import konjetic.sofanbaapp.databinding.FragmentPlayerDetailsBinding
import konjetic.sofanbaapp.network.model.PlayerImgPost
import konjetic.sofanbaapp.viewmodel.PlayerActivityViewModel

class PlayerPhotoBottomSheetFragment(private val bindingDetails: FragmentPlayerDetailsBinding) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)

        binding.buttons.cancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.buttons.addButton.setOnClickListener {
            if (binding.imageTitle.text.toString() != "" && binding.imageUrl.text.toString() != "") {
                viewModel.addPlayerImage(
                    PlayerImgPost(
                        PlayerActivity.player.id, binding.imageUrl.text.toString(), binding.imageTitle.text.toString()
                    )
                )
                viewModel.getPlayerImages(PlayerActivity.player.id)
                bindingDetails.playerDetailsCard.viewpager.refreshDrawableState()

                Handler().postDelayed({
                    this.dismiss()
                }, 1000)
            }
        }

        return binding.root
    }
}
