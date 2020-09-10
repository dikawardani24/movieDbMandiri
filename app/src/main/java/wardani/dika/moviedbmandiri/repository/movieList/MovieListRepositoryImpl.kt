package wardani.dika.moviedbmandiri.repository.movieList

import com.google.gson.reflect.TypeToken
import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.api.MovieDbConfig
import wardani.dika.moviedbmandiri.api.response.MovieResponse
import wardani.dika.moviedbmandiri.api.response.PageResponse
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.model.Page
import wardani.dika.moviedbmandiri.repository.BaseRepository
import wardani.dika.moviedbmandiri.util.JSonHelper
import wardani.dika.moviedbmandiri.util.MapperList
import wardani.dika.moviedbmandiri.util.Result

class MovieListRepositoryImpl(
        private val movieDbApi: MovieDbApi
) : BaseRepository(), MovieListRepository {

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
}