package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse
import wardani.dika.moviedbmandiri.api.response.MovieResponse
import wardani.dika.moviedbmandiri.util.DateFormatterHelper
import wardani.dika.moviedbmandiri.util.MapperList
import java.math.BigInteger
import java.util.*

data class Movie(
        var id: BigInteger,
        var genres: List<Genre>,
        var movieTarget: MovieTarget,
        var movieImage: MovieImage?,
        var belongToCollection: BelongToCollection?,
        var budget: BigInteger,
        var homePage: String?,
        var imdbId: String?,
        var title: String,
        var originalLanguage: String,
        var overview: String,
        var popularity: Double,
        var productionCompanies: List<ProductionCompany>,
        var productionCountries: List<ProductionCountry>,
        var releaseDate: Date,
        var revenue: BigInteger,
        var runtime: Int?,
        var spokenLanguages: List<SpokenLanguage>,
        var movieStatus: MovieStatus,
        var tagline: String?,
        var originalTitle: String,
        var hasVideo: Boolean,
        var vote: Vote
) {
    companion object {

        fun from(movieItem: MovieResponse): Movie {
            val image = MovieImage.from(movieItem)
            val vote = Vote.from(movieItem)
            val genres = MapperList.map(movieItem.genreIds) {
                Genre.from(it)
            }

            return Movie(
                    id = movieItem.id,
                    genres = genres,
                    hasVideo = movieItem.video,
                    movieImage = image,
                    movieStatus = MovieStatus.UNKNOWN,
                    movieTarget = MovieTarget.from(movieItem),
                    originalLanguage = movieItem.originalLanguage,
                    originalTitle = movieItem.originalTitle,
                    overview = movieItem.overview,
                    popularity = movieItem.popularity,
                    releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
                    title = movieItem.title,
                    vote = vote,
                    belongToCollection = null,
                    budget = BigInteger.ZERO,
                    homePage = null,
                    imdbId = null,
                    productionCompanies = emptyList(),
                    productionCountries = emptyList(),
                    revenue = BigInteger.ZERO,
                    runtime = null,
                    spokenLanguages = emptyList(),
                    tagline = null
            )
        }

        fun from(movieItem: MovieDetailResponse): Movie {
            val image = MovieImage.from(movieItem)
            val vote = Vote.from(movieItem)
            val collection = BelongToCollection.from(movieItem)

            val productionCompany = MapperList.map(movieItem.productionCompanies) {
                ProductionCompany.from(it)
            }

            val genres = MapperList.map(movieItem.genres) {
                Genre.from(it)
            }

            val productionCountry = MapperList.map(movieItem.productionCountries) {
                ProductionCountry.from(it)
            }

            val spokenLanguage = MapperList.map(movieItem.spokenLanguages) {
                SpokenLanguage.from(it)
            }

            return Movie(
                    id = movieItem.id,
                    budget = movieItem.budget,
                    belongToCollection = collection,
                    genres = genres,
                    hasVideo = movieItem.video,
                    homePage = movieItem.homepage,
                    imdbId = movieItem.imdbId,
                    movieImage = image,
                    movieStatus = MovieStatus.UNKNOWN,
                    movieTarget = MovieTarget.from(movieItem),
                    originalLanguage = movieItem.originalLanguage,
                    originalTitle = movieItem.originalTitle,
                    overview = movieItem.overview ?: "",
                    popularity = movieItem.popularity,
                    productionCompanies = productionCompany,
                    productionCountries = productionCountry,
                    releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
                    revenue = movieItem.revenue,
                    runtime = movieItem.runtime,
                    spokenLanguages = spokenLanguage,
                    tagline = movieItem.tagline,
                    title = movieItem.title,
                    vote = vote
            )
        }

    }
}