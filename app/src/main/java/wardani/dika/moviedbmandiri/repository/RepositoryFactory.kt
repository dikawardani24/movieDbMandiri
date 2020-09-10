package wardani.dika.moviedbmandiri.repository

import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.repository.genre.GenreRepository
import wardani.dika.moviedbmandiri.repository.genre.GenreRepositoryImpl
import wardani.dika.moviedbmandiri.repository.movieDetail.MovieDetailRepository
import wardani.dika.moviedbmandiri.repository.movieDetail.MovieDetailRepositoryImpl
import wardani.dika.moviedbmandiri.repository.movieList.MovieListRepository
import wardani.dika.moviedbmandiri.repository.movieList.MovieListRepositoryImpl

object RepositoryFactory {
    fun createMovieDetailRepository(movieDbApi: MovieDbApi): MovieDetailRepository {
        return MovieDetailRepositoryImpl(movieDbApi)
    }
    fun createGenreRepository(movieDbApi: MovieDbApi): GenreRepository {
        return GenreRepositoryImpl(movieDbApi)
    }
    fun createMovieListRepository(movieDbApi: MovieDbApi): MovieListRepository {
        return MovieListRepositoryImpl(movieDbApi)
    }
}