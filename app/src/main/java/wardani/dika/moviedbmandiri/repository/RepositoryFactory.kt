package wardani.dika.moviedbmandiri.repository

import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.repository.movie.MovieRepository
import wardani.dika.moviedbmandiri.repository.movie.MovieRepositoryImpl

object RepositoryFactory {
    fun createMovieRepository(movieDbApi: MovieDbApi): MovieRepository {
        return MovieRepositoryImpl(movieDbApi)
    }
}