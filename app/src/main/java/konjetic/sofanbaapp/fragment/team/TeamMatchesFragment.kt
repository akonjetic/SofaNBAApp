package konjetic.sofanbaapp.fragment.team

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.activity.TeamActivity
import konjetic.sofanbaapp.adapter.teamActivity.TeamMatchPagingAdapter
import konjetic.sofanbaapp.databinding.FragmentTeamMatchesBinding
import konjetic.sofanbaapp.network.paging.MatchDiff
import konjetic.sofanbaapp.viewmodel.TeamActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
class TeamMatchesFragment : Fragment() {

    private var _binding: FragmentTeamMatchesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TeamActivityViewModel by activityViewModels()
    private var playoff = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamMatchesBinding.inflate(layoutInflater, container, false)

        binding.matchesRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.headerAction.filter.visibility = View.GONE

        binding.headerAction.buttonRegular.isSelected = true
        TeamActivity.postseason = playoff

        setupPlaceholder()

        binding.placeholderEmpty.placeholder.visibility = View.GONE

        val pagingAdapter = TeamMatchPagingAdapter(requireContext(), MatchDiff)
        binding.matchesRecycler.adapter = pagingAdapter

        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                pagingAdapter.submitData(it)
            }
        }

        binding.headerAction.buttonRegular.setOnClickListener {
            if (binding.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                lifecycleScope.launch {
                    viewModel.flow.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            }
            if (binding.headerAction.buttonRegular.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.buttonRegular.isSelected = false
                playoff = true
                lifecycleScope.launch {
                    viewModel.flow2.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                binding.headerAction.buttonRegular.isSelected = true
                playoff = false
                lifecycleScope.launch {
                    viewModel.flow.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        binding.headerAction.buttonPlayoffs.setOnClickListener {
            if (binding.headerAction.buttonPlayoffs.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = false
                playoff = false
                lifecycleScope.launch {
                    viewModel.flow.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            }
            if (binding.headerAction.buttonRegular.isSelected) {
                binding.headerAction.buttonPlayoffs.isSelected = true
                binding.headerAction.buttonRegular.isSelected = false
                playoff = true
                lifecycleScope.launch {
                    viewModel.flow2.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                binding.headerAction.buttonRegular.isSelected = true
                playoff = false
                lifecycleScope.launch {
                    viewModel.flow.collectLatest {
                        pagingAdapter.submitData(PagingData.empty())
                        pagingAdapter.submitData(it)
                        pagingAdapter.notifyDataSetChanged()
                    }
                }
            }

            if (pagingAdapter.itemCount < 1) {
                binding.placeholderEmpty.placeholder.visibility = View.GONE
            } else {
                binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
            }

            pagingAdapter.addLoadStateListener { loadState ->

                if (loadState.source.refresh is LoadState.NotLoading) {
                    if (pagingAdapter.itemCount < 1) {
                        binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                    } else {
                        binding.placeholderEmpty.placeholder.visibility = View.GONE
                    }
                }

                if (loadState.append.endOfPaginationReached) {
                    if (pagingAdapter.itemCount < 1)
                        binding.placeholderEmpty.placeholder.visibility = View.VISIBLE
                    else
                        binding.placeholderEmpty.placeholder.visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun setupPlaceholder() {
        binding.placeholderEmpty.placeholderText.text = getString(R.string.placeholderFilter1)
    }
}
