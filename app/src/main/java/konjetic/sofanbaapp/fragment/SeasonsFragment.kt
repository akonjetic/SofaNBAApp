package konjetic.sofanbaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.MainActivityViewModel
import konjetic.sofanbaapp.adapter.MatchPagingAdapter
import konjetic.sofanbaapp.adapter.PlayerPagingAdapter
import konjetic.sofanbaapp.databinding.FragmentSeasonsBinding
import konjetic.sofanbaapp.network.paging.MatchDiff
import konjetic.sofanbaapp.network.paging.PlayerDiff
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SeasonsFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)

        binding.seasonsRecycler.layoutManager = LinearLayoutManager(requireContext())

        val pagingAdapter = MatchPagingAdapter(requireContext(), MatchDiff)
        binding.seasonsRecycler.adapter = pagingAdapter

        lifecycleScope.launch {
            viewModel.flow2.collectLatest {
                pagingAdapter.submitData(it)
            }
        }

        return binding.root
    }


}