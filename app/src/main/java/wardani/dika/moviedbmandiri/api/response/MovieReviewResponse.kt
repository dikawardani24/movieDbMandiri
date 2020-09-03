package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName
import wardani.dika.moviedbmandiri.api.response.ReviewResponse

data class MovieReviewResponse(
        @SerializedName("id")
    val id : Int,
        @SerializedName("page")
    val page : Int,
        @SerializedName("results")
    val reviews : List<ReviewResponse>,
        @SerializedName("total_pages")
    val totalPages : Int,
        @SerializedName("total_results")
    val totalResults : Int
)