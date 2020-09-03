package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.MovieDbConfig.imageBaseUrl
import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse
import wardani.dika.moviedbmandiri.api.response.MovieResponse

data class MovieImage(
    var backDropPath: String?,
    var posterPath: String?
) {
    companion object{
        fun from(movie: MovieResponse): MovieImage {
            return MovieImage(
                backDropPath = "${imageBaseUrl}${movie.backDropPath}",
                posterPath = "${imageBaseUrl}${movie.posterPath}"
            )
        }

        fun from(movieItem: MovieDetailResponse): MovieImage? {
            return if (movieItem.backdropPath == null && movieItem.posterPath == null) null
            else MovieImage(
                    backDropPath = "${imageBaseUrl}${movieItem.backdropPath}",
                    posterPath = "${imageBaseUrl}${movieItem.posterPath}"
            )
        }
    }
}