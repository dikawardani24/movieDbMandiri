package wardani.dika.moviedbmandiri.di.module

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import wardani.dika.moviedbmandiri.api.MovieDbApi
import wardani.dika.moviedbmandiri.di.qualifier.ApplicationContext
import wardani.dika.moviedbmandiri.di.scope.ApplicationScope

@Module
class RetrofitModule {

    @Provides
    @ApplicationScope
    fun getInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @ApplicationScope
    fun getCookieJar(context: Context): CookieJar {
        val cache = SetCookieCache()
        val persistor = SharedPrefsCookiePersistor(context)
        return PersistentCookieJar(cache, persistor)
    }

    @Provides
    @ApplicationScope
    fun getClient(interceptor: Interceptor, cookieJar: CookieJar): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cookieJar(cookieJar)
            .build()
    }

    @Provides
    @ApplicationScope
    fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @ApplicationContext
    fun getMovieDdApi(retrofit: Retrofit): MovieDbApi {
        return retrofit.create(MovieDbApi::class.java)
    }

    companion object {
        private const val BASE_URL = " https://api.themoviedb.org/3/"
    }
}