package konjetic.sofanbaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import konjetic.sofanbaapp.activity.MainActivityViewModel
import konjetic.sofanbaapp.adapter.FavoritePlayerAdapter
import konjetic.sofanbaapp.adapter.FavoriteTeamAdapter
import konjetic.sofanbaapp.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritePlayerAdapter by lazy { FavoritePlayerAdapter(requireContext(), arrayListOf()) }
    private val favoriteTeamAdapter by lazy { FavoriteTeamAdapter(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.recyclerViewP.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewP.adapter = favoritePlayerAdapter

        binding.recyclerViewT.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewT.adapter = favoriteTeamAdapter

        viewModel.favoritePlayersFromDB.observe(viewLifecycleOwner){
            favoritePlayerAdapter.updateFavorites(it)

            for(item in it){
                viewModel.getPlayerImages(item.id)
            }
        }

        viewModel.favoriteTeamsFromDB.observe(viewLifecycleOwner){
            favoriteTeamAdapter.updateFavorites(it)

        }

        viewModel.playerImages.observe(viewLifecycleOwner){
            favoritePlayerAdapter.updateImgList(it)
        }

        viewModel.getFavoritePlayersFromDB(requireContext())
        viewModel.getFavoriteTeamsFromDB(requireContext())

        return  binding.root
    }


}