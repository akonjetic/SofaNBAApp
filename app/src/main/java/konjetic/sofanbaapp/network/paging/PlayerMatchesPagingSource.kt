package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.network.model.BallDontLieService
import konjetic.sofanbaapp.network.model.StatsResponseData
import java.lang.Exception

class PlayerMatchesPagingSource(private val service: BallDontLieService, val playerId: Long, private val postseason: Boolean, val season: Int) : PagingSource<Int, StatsResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, StatsResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StatsResponseData> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = service.getSeasonStatsPaged(20, nextPageNumber, playerId, postseason, season)

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = if (response.meta.next_page == 0) null else response.meta.next_page
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
