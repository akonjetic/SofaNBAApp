package konjetic.sofanbaapp.network.model

import java.io.Serializable

data class HighlightResponse(
    val data: ArrayList<HighlightResponseData>
) : Serializable

data class HighlightResponseData(
    val id: Long,
    val eventId: Int,
    val startTimestamp: Int,
    val name: String,
    val url: String,
    val playerIdList: ArrayList<Int>?
) : Serializable

data class HighlightResponseDataPost(
    val eventId: Int,
    val startTimestamp: Int,
    val name: String,
    val url: String,
    val playerIdList: ArrayList<Int>?
) : Serializable
