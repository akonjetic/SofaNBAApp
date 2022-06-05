package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.network.model.BallDontLieService
import java.lang.Exception

class MatchPagingSource(val service: BallDontLieService) : PagingSource<Int, MatchResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, MatchResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchResponseData> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = service.getAllGames(20, nextPageNumber)

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = response.meta.next_page
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
