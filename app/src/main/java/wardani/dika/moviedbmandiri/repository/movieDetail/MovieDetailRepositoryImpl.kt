package wardani.dika.moviedbmandiri.repository.movieDetail

import com.google.gson.reflect.TypeToken
import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.api.MovieDbConfig
import wardani.dika.moviedbmandiri.api.response.ListVideoResponse
import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse
import wardani.dika.moviedbmandiri.api.response.PageResponse
import wardani.dika.moviedbmandiri.api.response.ReviewResponse
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.model.Page
import wardani.dika.moviedbmandiri.model.Review
import wardani.dika.moviedbmandiri.model.Video
import wardani.dika.moviedbmandiri.repository.BaseRepository
import wardani.dika.moviedbmandiri.util.JSonHelper
import wardani.dika.moviedbmandiri.util.MapperList
import wardani.dika.moviedbmandiri.util.Result
import java.math.BigInteger

class MovieDetailRepositoryImpl(
    private val movieDbApi: MovieDbApi
) : BaseRepository(), MovieDetailRepository {

    override suspend fun getMovieDetail(movieId: BigInteger): Result<Movie> {
        return try {
            val json = movieDbApi.getDetailMovie(
                apiKey = MovieDbConfig.API_KEY,
                language = MovieDbConfig.LANGUAGE,
                movieId = movieId
            )

            val response = JSonHelper.fromJson(MovieDetailResponse::class.java, json)
            val movie = Movie.from(response)
            Result.Success(movie)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getMovieReviews(movie: Movie, pageNumber: Int): Result<Page<Review>> {
        return try {
            val json = movieDbApi.getMovieReviews(
                apiKey = MovieDbConfig.API_KEY,
                language = MovieDbConfig.LANGUAGE,
                page = pageNumber,
                movieId = movie.id
            )

            val type = object : TypeToken<PageResponse<ReviewResponse>>() {}.type
            val response = JSonHelper.fromJson<PageResponse<ReviewResponse>>(type, json)
            val reviews = MapperList.map(response.results) {
                Review.from(it, movie)
            }

            val page = Page(
                content = reviews,
                page = response.page,
                totalPages = 0,
                totalResults = 0
            )
            Result.Success(page)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getVideos(movie: Movie): Result<List<Video>> {
        return try {
            val json = movieDbApi.getMovieTrailer(
                movieId = movie.id,
                language = MovieDbConfig.LANGUAGE,
                apiKey = MovieDbConfig.API_KEY
            )

            val response = JSonHelper.fromJson(ListVideoResponse::class.java, json)
            val videos = MapperList.map(response.results) {
                Video.from(it, movie)
            }

            Result.Success(videos)
        } catch (e: Exception) {
            handleException(e)
        }
    }
}