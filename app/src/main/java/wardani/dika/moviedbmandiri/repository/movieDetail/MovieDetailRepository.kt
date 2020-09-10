package wardani.dika.moviedbmandiri.repository.movieDetail

import wardani.dika.moviedbmandiri.model.*
import wardani.dika.moviedbmandiri.util.Result
import java.math.BigInteger

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: BigInteger): Result<Movie>
    suspend fun getMovieReviews(movie: Movie, pageNumber: Int): Result<Page<Review>>
    suspend fun getVideos(movie: Movie): Result<List<Video>>
}