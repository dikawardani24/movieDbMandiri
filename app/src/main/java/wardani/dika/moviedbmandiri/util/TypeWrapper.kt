package wardani.dika.moviedbmandiri.util

@Suppress("CanSealedSubClassBeObject")
sealed class TypeWrapper<T> {
    data class DataWrapper<T>(val value: T): TypeWrapper<T>()
    class LoadingWrapper<T>: TypeWrapper<T>()
    class NoMoreDataWrapper<T>: TypeWrapper<T>()
    class ErrorWrapper<T>: TypeWrapper<T>()
}