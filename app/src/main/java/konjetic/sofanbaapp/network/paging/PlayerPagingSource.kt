package konjetic.sofanbaapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import konjetic.sofanbaapp.activity.MainActivityViewModel
import konjetic.sofanbaapp.network.model.BallDontLieService
import konjetic.sofanbaapp.network.model.PlayersResponseData

class PlayerPagingSource(val service1: BallDontLieService, val viewModel: MainActivityViewModel) : PagingSource<Int, PlayersResponseData>() {
    override fun getRefreshKey(state: PagingState<Int, PlayersResponseData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayersResponseData> {
        return try{
            val nextPageNumber = params.key ?: 0
            val response = service1.getAllPlayers(20, nextPageNumber)
            for(item in response.data){
                try {
                    viewModel.getPlayerImages(item.id)
                } catch (e: Exception){
                    print(e)
                }
            }


            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = response.meta.next_page
                /*data = response.playersResponseData,
                prevKey = null,
                nextKey = response.playersResponseMeta.next_page*/
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }

}