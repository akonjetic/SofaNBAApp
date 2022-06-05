package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.network.model.BallDontLieService

class TeamMatchPagingSourcePlayoffs(private val service1: BallDontLieService, val teamId: Long) : PagingSource<Int, MatchResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, MatchResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchResponseData> {
        return try {
            val meta = service1.getGamesOfTeam(teamId, true)
            val nextPageNumber = params.key ?: meta.meta.total_pages
            val response = service1.getGamesOfTeamPaged(teamId, 25, nextPageNumber, true)

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = response.meta.current_page - 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
