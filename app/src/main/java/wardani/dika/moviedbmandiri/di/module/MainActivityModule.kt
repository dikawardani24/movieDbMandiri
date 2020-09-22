package wardani.dika.moviedbmandiri.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import wardani.dika.moviedbmandiri.di.qualifier.ActivityContext
import wardani.dika.moviedbmandiri.di.scope.ActivityScope
import wardani.dika.moviedbmandiri.ui.activity.MainActivity

@Module
class MainActivityModule(val mainActivity: MainActivity) {
    val context: Context = mainActivity

    @Provides
    @ActivityScope
    fun provideMainActivity(): MainActivity {
        return mainActivity
    }

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideContext(): Context {
        return context
    }
}