package konjetic.sofanbaapp.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.AboutActivity
import konjetic.sofanbaapp.databinding.FragmentSettingsBinding
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.clearFavorites.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder
                .setTitle(getString(R.string.clearFavoritesAlertTitle))
                .setPositiveButton(
                    getString(R.string.clearFavoritesAlertClear)
                ) { _, _ ->
                    viewModel.removeFromFavorites(requireContext())
                    Snackbar.make(binding.root, getString(R.string.clearFavoritesSnackbar), Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.clearFavoritesAlertCancel)) { _, _ ->
                }
                .create()

            dialog.show()

            setDialogDesign(requireContext(), dialog)
        }

        binding.moreInfoTV.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            requireContext().startActivity(intent)
        }
        return binding.root
    }

    private fun setDialogDesign(context: Context, dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(context.getColor(R.color.color_primary))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.white))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.color_primary))
    }
}
