package konjetic.sofanbaapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import konjetic.sofanbaapp.R
import konjetic.sofanbaapp.adapter.FavoritePlayerAdapter
import konjetic.sofanbaapp.adapter.FavoriteTeamAdapter
import konjetic.sofanbaapp.database.entity.PlayerData
import konjetic.sofanbaapp.database.entity.TeamData
import konjetic.sofanbaapp.databinding.FragmentFavoritesBinding
import konjetic.sofanbaapp.network.model.PlayerImgResponseData
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class FavoritesFragment : Fragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritePlayerAdapter by lazy { FavoritePlayerAdapter(requireContext(), arrayListOf()) }
    private val favoriteTeamAdapter by lazy { FavoriteTeamAdapter(requireContext(), arrayListOf()) }

    val favPlayers = arrayListOf<PlayerData>()
    val favTeams = arrayListOf<TeamData>()
    private val playerImgs = arrayListOf<PlayerImgResponseData>()

    private var editable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.recyclerViewP.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewP.adapter = favoritePlayerAdapter

        binding.recyclerViewT.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewT.adapter = favoriteTeamAdapter

        val itemTouchHelperPlayer = ItemTouchHelper(ItemTouchHelperCallbackPlayer)
        val itemTouchHelperTeam = ItemTouchHelper(ItemTouchHelperCallbackTeam)

        if (favPlayers.isNullOrEmpty()) {
            binding.recyclerViewP.visibility = View.GONE
            binding.placeholderPlayers.placeholder.visibility = View.VISIBLE
            binding.placeholderPlayers.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle1)
        } else {
            binding.placeholderPlayers.placeholder.visibility = View.GONE
            binding.recyclerViewP.visibility = View.VISIBLE
        }

        if (favTeams.isNullOrEmpty()) {
            binding.recyclerViewT.visibility = View.GONE
            binding.placeholderTeams.placeholder.visibility = View.VISIBLE
            binding.placeholderTeams.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle2)
        } else {
            binding.recyclerViewT.visibility = View.VISIBLE
            binding.placeholderTeams.placeholder.visibility = View.GONE
        }

        binding.editIcon.setOnClickListener {
            if (favPlayers.isNotEmpty() || favTeams.isNotEmpty()) {
                if (!editable) {
                    editable = true
                    binding.editIcon.isActivated = editable
                    itemTouchHelperPlayer.attachToRecyclerView(binding.recyclerViewP)
                    itemTouchHelperTeam.attachToRecyclerView(binding.recyclerViewT)
                } else {
                    editable = false
                    binding.editIcon.isActivated = editable

                    itemTouchHelperPlayer.attachToRecyclerView(null)
                    itemTouchHelperTeam.attachToRecyclerView(null)
                }
                favoritePlayerAdapter.updateReorder()
                favoriteTeamAdapter.updateReorder()
            }
        }

        viewModel.favoritePlayersFromDB.observe(viewLifecycleOwner) { players ->
            if (players.isNullOrEmpty()) {
                binding.placeholderPlayers.placeholder.visibility = View.VISIBLE
                binding.recyclerViewP.visibility = View.GONE
                binding.placeholderPlayers.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle1)
            } else {
                binding.placeholderPlayers.placeholder.visibility = View.GONE
                binding.recyclerViewP.visibility = View.VISIBLE
            }

            favPlayers.clear()
            favPlayers.addAll(players)
            favoritePlayerAdapter.updateFavorites(favPlayers)

            if (favPlayers.isNullOrEmpty()) {
                binding.placeholderPlayers.placeholder.visibility = View.VISIBLE
                binding.recyclerViewP.visibility = View.GONE
                binding.placeholderPlayers.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle1)
            } else {
                binding.placeholderPlayers.placeholder.visibility = View.GONE
                binding.recyclerViewP.visibility = View.VISIBLE
            }
        }

        for (item in favPlayers) {
            viewModel.getPlayerImages(item.id)
        }

        viewModel.favoriteTeamsFromDB.observe(viewLifecycleOwner) { teams ->
            favTeams.clear()
            favTeams.addAll(teams)
            favoriteTeamAdapter.updateFavorites(favTeams)

            if (favTeams.isNullOrEmpty()) {
                binding.recyclerViewT.visibility = View.GONE
                binding.placeholderTeams.placeholder.visibility = View.VISIBLE
                binding.placeholderTeams.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle2)
            } else {
                binding.recyclerViewT.visibility = View.VISIBLE
                binding.placeholderTeams.placeholder.visibility = View.GONE
            }
        }

        viewModel.playerImages.observe(viewLifecycleOwner) {
            playerImgs.clear()
            playerImgs.addAll(it)
            favoritePlayerAdapter.updateImagesList(playerImgs)
        }

        return binding.root
    }

    override fun onResume() {

        viewModel.getFavoriteTeamsFromDB(requireContext())
        viewModel.getFavoritePlayersFromDB(requireContext())

        editable = false
        binding.editIcon.isActivated = editable
        favoritePlayerAdapter.reorder = true
        favoriteTeamAdapter.reorder = true
        favoriteTeamAdapter.updateReorder()
        favoritePlayerAdapter.updateReorder()

        if (favPlayers.isNullOrEmpty()) {
            binding.recyclerViewP.visibility = View.GONE
            binding.placeholderPlayers.placeholder.visibility = View.VISIBLE
            binding.placeholderPlayers.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle1)
        } else {
            binding.placeholderPlayers.placeholder.visibility = View.GONE
            binding.recyclerViewP.visibility = View.VISIBLE
        }

        if (favTeams.isNullOrEmpty()) {
            binding.recyclerViewT.visibility = View.GONE
            binding.placeholderTeams.placeholder.visibility = View.VISIBLE
            binding.placeholderTeams.placeholderText.text = requireContext().getString(R.string.placeholderFavoriteTitle2)
        } else {
            binding.recyclerViewT.visibility = View.VISIBLE
            binding.placeholderTeams.placeholder.visibility = View.GONE
        }

        super.onResume()
    }

    private val ItemTouchHelperCallbackPlayer = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val flags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            viewModel.insertAllFavoritePlayers(requireContext(), favPlayers)
            return makeMovementFlags(flags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition
            if (startPosition < endPosition) {
                for (i in startPosition until endPosition) {
                    Collections.swap(favPlayers, i, i + 1)
                }
            } else {
                for (i in endPosition until startPosition) {
                    Collections.swap(favPlayers, i, i + 1)
                }
            }
            (binding.recyclerViewP.adapter as FavoritePlayerAdapter).swapItems(
                startPosition,
                endPosition
            )

            favPlayers[startPosition].positionDB = startPosition
            favPlayers[endPosition].positionDB = endPosition

            viewModel.insertFavoritePlayerToDB(requireContext(), favPlayers[startPosition])
            viewModel.insertFavoritePlayerToDB(requireContext(), favPlayers[endPosition])

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            favoritePlayerAdapter.removeItem(position)
            favoritePlayerAdapter.notifyDataSetChanged()
        }
    }

    private val ItemTouchHelperCallbackTeam = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val flags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(flags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition
            if (startPosition < endPosition) {
                for (i in startPosition until endPosition) {
                    Collections.swap(favTeams, i, i + 1)
                }
            } else {
                for (i in endPosition until startPosition) {
                    Collections.swap(favTeams, i, i + 1)
                }
            }

            (binding.recyclerViewT.adapter as FavoriteTeamAdapter).swapItems(
                startPosition,
                endPosition
            )

            favTeams[startPosition].position = startPosition
            favTeams[endPosition].position = endPosition

            viewModel.insertFavoriteTeamToDB(requireContext(), favTeams[startPosition])
            viewModel.insertFavoriteTeamToDB(requireContext(), favTeams[endPosition])

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            favoriteTeamAdapter.removeItem(position)
            favoriteTeamAdapter.notifyDataSetChanged()
        }
    }
}
