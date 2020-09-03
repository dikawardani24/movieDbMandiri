package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse
import wardani.dika.moviedbmandiri.api.response.MovieResponse

data class Vote(
        var average: Double,
        var count: Int
) {
    companion object {
        fun from(movieDetailResponse: MovieDetailResponse): Vote {
            return Vote(
                    average = movieDetailResponse.voteAverage,
                    count = movieDetailResponse.voteCount
            )
        }

        fun from(movieResponse: MovieResponse): Vote {
            return Vote(
                    average = movieResponse.voteAverage,
                    count = movieResponse.voteCount
            )
        }
    }
}