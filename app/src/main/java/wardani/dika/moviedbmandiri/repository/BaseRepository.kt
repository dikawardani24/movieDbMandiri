package wardani.dika.moviedbmandiri.repository

import retrofit2.HttpException
import wardani.dika.moviedbmandiri.api.response.ErrorResponse
import wardani.dika.moviedbmandiri.exception.ApiException
import wardani.dika.moviedbmandiri.exception.ConnectException
import wardani.dika.moviedbmandiri.exception.SystemException
import wardani.dika.moviedbmandiri.util.JSonHelper
import wardani.dika.moviedbmandiri.util.Result
import java.net.UnknownHostException

abstract class BaseRepository {

    protected fun <T> handleException(e: Throwable): Result.Failed<T> {
        val throwable: Throwable =
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = JSonHelper.fromJson(ErrorResponse::class.java, errorBody)
                        ApiException(errorResponse.statusMessage)
                    } else {
                        ApiException("Unable to fetch data caused by ${e.message()}")
                    }
                } else if (e is UnknownHostException) {
                    ConnectException("Unable to connect to server, please check your internet connection")
                } else {
                    SystemException("System error caused by ${e.message}")
                }

        return Result.Failed(throwable)
    }

}