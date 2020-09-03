package wardani.dika.moviedbmandiri.repository.movie

import wardani.dika.moviedbmandiri.model.*
import wardani.dika.moviedbmandiri.util.Result
import java.math.BigInteger

interface MovieRepository {
    suspend fun getPageMoviesByGenre(genre: Genre, pageNumber: Int): Result<Page<Movie>>
    suspend fun getListGenre(): Result<List<Genre>>
    suspend fun getMovieDetail(movieId: BigInteger): Result<Movie>
    suspend fun getMovieReviews(movie: Movie, pageNumber: Int): Result<Page<Review>>
    suspend fun getVideos(movie: Movie): Result<List<Video>>
}