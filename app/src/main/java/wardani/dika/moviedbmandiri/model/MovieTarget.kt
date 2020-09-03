package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse
import wardani.dika.moviedbmandiri.api.response.MovieResponse

enum class MovieTarget(val value: String) {
    ALL_AGE("All Age"),
    ADULT("Adult");

    companion object {
        private fun toMovieItem(adult: Boolean): MovieTarget {
            return if (adult) ADULT else ALL_AGE
        }

        fun from(movieItem: MovieResponse): MovieTarget {
            return toMovieItem(movieItem.adult)
        }

        fun from(movieItem: MovieDetailResponse): MovieTarget {
            return toMovieItem(movieItem.adult)
        }
    }
}