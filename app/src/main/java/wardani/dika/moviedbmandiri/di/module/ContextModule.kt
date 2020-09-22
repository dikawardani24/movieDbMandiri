package wardani.dika.moviedbmandiri.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import wardani.dika.moviedbmandiri.di.qualifier.ApplicationContext
import wardani.dika.moviedbmandiri.di.scope.ApplicationScope

@Module
class ContextModule (val context: Context) {

    @Provides
    @ApplicationContext
    @ApplicationScope
    fun provideContext(): Context {
        return context
    }
}