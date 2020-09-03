package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class MovieDetailResponse(
        @SerializedName("adult")
        val adult: Boolean,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("belongs_to_collection")
        val belongsToCollection: BelongToCollectionResponse?,
        @SerializedName("budget")
        val budget: BigInteger,
        @SerializedName("genres")
        val genres: List<GenreResponse>,
        @SerializedName("homepage")
        val homepage: String?,
        @SerializedName("id")
        val id: BigInteger,
        @SerializedName("imdb_id")
        val imdbId: String?,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("production_companies")
        val productionCompanies: List<ProductionCompanyResponse>,
        @SerializedName("production_countries")
        val productionCountries: List<ProductionCountryResponse>,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("revenue")
        val revenue: BigInteger,
        @SerializedName("runtime")
        val runtime: Int?,
        @SerializedName("spoken_languages")
        val spokenLanguages: List<SpokenLanguageResponse>,
        @SerializedName("status")
        val status: String,
        @SerializedName("tagline")
        val tagline: String?,
        @SerializedName("title")
        val title: String,
        @SerializedName("video")
        val video: Boolean,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
)