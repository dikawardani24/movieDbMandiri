package wardani.dika.moviedbmandiri.repository.genre

import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.api.MovieDbConfig
import wardani.dika.moviedbmandiri.api.response.ListGenreResponse
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.repository.BaseRepository
import wardani.dika.moviedbmandiri.util.JSonHelper
import wardani.dika.moviedbmandiri.util.MapperList
import wardani.dika.moviedbmandiri.util.Result

class GenreRepositoryImpl(
        private val movieDbApi: MovieDbApi
) : BaseRepository(), GenreRepository {

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
}