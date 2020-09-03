package wardani.dika.moviedbmandiri.repository.movie

import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.api.MovieDbConfig
import wardani.dika.moviedbmandiri.api.response.*
import wardani.dika.moviedbmandiri.exception.ApiException
import wardani.dika.moviedbmandiri.exception.ConnectException
import wardani.dika.moviedbmandiri.exception.SystemException
import wardani.dika.moviedbmandiri.model.*
import wardani.dika.moviedbmandiri.util.JSonHelper
import wardani.dika.moviedbmandiri.util.MapperList
import wardani.dika.moviedbmandiri.util.Result
import java.math.BigInteger
import java.net.UnknownHostException

class MovieRepositoryImpl(
    private val movieDbApi: MovieDbApi
) : MovieRepository {
    private fun <T> handleException(e: Throwable): Result.Failed<T> {
        val throwable: Throwable =
        if (e is HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val errorResponse = JSonHelper.fromJson(ErrorResponse::class.java, errorBody)
                ApiException(errorResponse.statusMessage)
            } else {
                ApiException("Unable to fetch data caused by ${e.message()}")
            }
        } else if (e is UnknownHostException) {
            ConnectException("Unable to connect to server, please check your internet connection")
        } else {
            SystemException("System error caused by ${e.message}")
        }

        return Result.Failed(throwable)
    }

    override suspend fun getPageMoviesByGenre(genre: Genre, pageNumber: Int): Result<Page<Movie>> {
        return try {
            val json = movieDbApi.getMoviesByGenre(
                apiKey = MovieDbConfig.API_KEY,
                language = MovieDbConfig.LANGUAGE,
                page = pageNumber,
                genreId = genre.id.toString()
            )

            val pageType = object : TypeToken<PageResponse<MovieResponse>>() {}.type
            val response = JSonHelper.fromJson<PageResponse<MovieResponse>>(pageType, json)

            val movies = MapperList.map(response.results) {
                Movie.from(it)
            }

            val page = Page(
                content = movies,
                page = response.page,
                totalPages = response.totalPages,
                totalResults = response.totalResults
            )

            Result.Success(page)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getListGenre(): Result<List<Genre>> {
        return try {
            val json = movieDbApi.getGenres(
                apiKey = MovieDbConfig.API_KEY,
                language = MovieDbConfig.LANGUAGE
            )
            val response = JSonHelper.fromJson(ListGenreResponse::class.java, json)
            val genres = MapperList.map(response.genres) {
                Genre.from(it)
            }
            Result.Success(genres)
        } catch (e: Exception) {
            handleException(e)
        }
    }

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