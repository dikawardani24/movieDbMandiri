package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName

data class ListVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<VideoResponse>
)