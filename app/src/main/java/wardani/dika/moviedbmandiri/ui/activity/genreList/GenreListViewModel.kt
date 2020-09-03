package wardani.dika.moviedbmandiri.ui.activity.genreList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.repository.movie.MovieRepository
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.util.Result

class GenreListViewModel(
    application: Application,
    private val movieRepository: MovieRepository
): AndroidViewModel(application) {
    val genreListLiveData: LiveData<State<List<Genre>>> = MutableLiveData()
    private var job: Job? = null

    fun loadDataGenre() {
        val liveData = genreListLiveData as MutableLiveData
        liveData.postValue(State.Loading())

        job = viewModelScope.launch {
            when(val result = movieRepository.getListGenre()) {
                is Result.Success -> {
                    val genres = result.data

                    if (genres.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        liveData.postValue(State.DataFetched(genres))
                    }
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                }
            }
        }
    }

    fun stop() {
        val currentJob = job
        if (currentJob != null && currentJob.isActive) {
            currentJob.cancel()
        }
    }
}