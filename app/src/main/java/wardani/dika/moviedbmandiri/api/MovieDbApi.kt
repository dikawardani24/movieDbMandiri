package wardani.dika.moviedbmandiri.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger

interface MovieDbApi {

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String,
                          @Query("language") language: String): String

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("with_genres")genreId: String
    ): String

    @GET("movie/{movieId}")
    suspend fun getDetailMovie(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): String

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): String

    @GET("movie/{movieId}/videos")
    suspend fun getMovieTrailer(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): String
}