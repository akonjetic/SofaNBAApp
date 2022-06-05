package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.fragment.MatchResponse
import konjetic.sofanbaapp.fragment.MatchResponseData
import konjetic.sofanbaapp.network.model.BallDontLieService

class SeasonMatchPagingFilteredSource(private val service1: BallDontLieService, val teamId: Long, private val playoff: Boolean, private val season: Int) : PagingSource<Int, MatchResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, MatchResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchResponseData> {
        return try {
            val nextPageNumber = params.key ?: 0

            var response: MatchResponse? = null

            if (teamId != 0L && season > 0) {
                response = service1.getGamesOfTeamSeasonPaged(teamId, 20, nextPageNumber, playoff, season)
            } else if (teamId == 0L && season > 0) {
                response = service1.getGamesOfSeasonPaged(20, nextPageNumber, playoff, season)
            } else if (teamId != 0L && season == 0) {
                response = service1.getGamesOfTeamPaged(teamId, 20, nextPageNumber, playoff)
            } else {
                response = service1.getAllGamesFiltered(20, nextPageNumber, playoff)
            }

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
