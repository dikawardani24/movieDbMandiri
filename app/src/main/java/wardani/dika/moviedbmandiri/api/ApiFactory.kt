package wardani.dika.moviedbmandiri.api

import android.content.Context

object ApiFactory {
    fun createMovieDbApi(context: Context): MovieDbApi {
        return MovieDbClient.createEndPoint(context = context, MovieDbApi::class.java)
    }
}