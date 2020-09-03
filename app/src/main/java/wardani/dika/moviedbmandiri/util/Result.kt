package wardani.dika.moviedbmandiri.util

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failed<T>(val throwable: Throwable): Result<T>()
}