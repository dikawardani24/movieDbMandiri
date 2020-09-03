package wardani.dika.moviedbmandiri.ui

abstract class State<T> {
    class Loading<T> : State<T>()
    class DataFetched<T>(val data: T): State<T>()
    class EmptyDataFetched<T>: State<T>()
    class ErrorOccurred<T>(val errorMessage: String): State<T>()
}