package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName

data class ListGenreResponse(
        @SerializedName("genres")
        val genres: List<GenreResponse>
)