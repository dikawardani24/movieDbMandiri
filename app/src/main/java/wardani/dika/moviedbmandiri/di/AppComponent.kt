package wardani.dika.moviedbmandiri.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}