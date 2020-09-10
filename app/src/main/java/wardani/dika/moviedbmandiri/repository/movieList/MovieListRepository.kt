package wardani.dika.moviedbmandiri.repository.movieList

import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.model.Page
import wardani.dika.moviedbmandiri.util.Result

interface MovieListRepository {
    suspend fun getPageMoviesByGenre(genre: Genre, pageNumber: Int): Result<Page<Movie>>
}