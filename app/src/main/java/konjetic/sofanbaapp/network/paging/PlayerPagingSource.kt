package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.network.model.BallDontLieService
import konjetic.sofanbaapp.network.model.PlayersResponseData
import konjetic.sofanbaapp.viewmodel.MainActivityViewModel

class PlayerPagingSource(private val service1: BallDontLieService, val viewModel: MainActivityViewModel) : PagingSource<Int, PlayersResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, PlayersResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayersResponseData> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = service1.getAllPlayers(20, nextPageNumber)

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = response.meta.current_page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
