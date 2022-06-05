package konjetic.sofanbaapp.fragment.match

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MatchActivity
import konjetic.sofanbaapp.databinding.BottomSheetLayoutMatchHighlightsBinding
import konjetic.sofanbaapp.network.model.HighlightResponseDataPost
import konjetic.sofanbaapp.viewmodel.MatchActivityViewModel

class MatchHighlightBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: MatchActivityViewModel by activityViewModels()
    private var _binding: BottomSheetLayoutMatchHighlightsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetLayoutMatchHighlightsBinding.inflate(layoutInflater, container, false)

        val match = MatchActivity.match

        binding.buttons.addButton.setOnClickListener {
            var valid = true
            var url = ""
            var title = ""
            if (binding.imageUrl.text.toString() != "" && isYoutubeUrl(binding.imageUrl.text.toString())) {
                url = binding.imageUrl.text.toString()
            } else {
                valid = false
                Snackbar.make(binding.root, getString(R.string.invalidUrlText), Snackbar.LENGTH_SHORT).show()
            }

            if (binding.videoText.text.toString() != "") {
                title = binding.videoText.text.toString()
            } else {
                valid = false
            }

            if (valid) {
                viewModel.postMatchHighlights(HighlightResponseDataPost(match.id.toInt(), 0, title, url, arrayListOf()))

                Handler().postDelayed({
                    this.dismiss()
                }, 1000)
            }
        }

        binding.buttons.cancelButton.setOnClickListener {
            this.dismiss()
        }

        return binding.root
    }

    private fun isYoutubeUrl(youTubeURl: String): Boolean {
        val success: Boolean
        val pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+"
        success = youTubeURl.isNotEmpty() && youTubeURl.matches(pattern.toRegex())
        return success
    }
}
