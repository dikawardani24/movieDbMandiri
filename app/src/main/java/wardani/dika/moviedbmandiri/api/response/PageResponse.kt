package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("results")
    val results: List<T>
)