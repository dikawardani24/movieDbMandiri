package wardani.dika.moviedbmandiri.repository.genre

import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.util.Result

interface GenreRepository {
    suspend fun getListGenre(): Result<List<Genre>>
}