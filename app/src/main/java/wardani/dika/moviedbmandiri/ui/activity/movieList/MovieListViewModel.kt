package wardani.dika.moviedbmandiri.ui.activity.movieList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.repository.movie.MovieRepository
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.util.Result

class MovieListViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val genre: Genre?
) : AndroidViewModel(application) {
    val loadMovieDataNew: LiveData<State<List<Movie>>> = MutableLiveData()
    val loadMoreMovieDataNew: LiveData<State<List<Movie>>> = MutableLiveData()

    private val movies = arrayListOf<Movie>()
    private var currentPage = 1
    private var job: Job? = null

    private fun loadMovieNew(liveData: MutableLiveData<State<List<Movie>>>) {
        liveData.postValue(State.Loading())

        val selectedGenre = genre
        if (selectedGenre != null) {
            job = viewModelScope.launch {
                val result = movieRepository.getPageMoviesByGenre(
                    genre = selectedGenre,
                    pageNumber = currentPage
                )

                when (result) {
                    is Result.Success -> {
                        val page = result.data
                        val content = page.content

                        if (content.isEmpty()) {
                            liveData.postValue(State.EmptyDataFetched())
                        } else {
                            movies.addAll(content)
                            liveData.postValue(State.DataFetched(movies))
                        }
                    }
                    is Result.Failed -> {
                        val error = result.throwable
                        error.printStackTrace()
                        currentPage -= 1
                        liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                    }
                }
            }
        } else {
            liveData.postValue(State.ErrorOccurred("Unable to load data movie caused by no data genre has been received"))
        }
    }

    fun loadDataMovie() {
        currentPage = 1
        loadMovieNew(loadMovieDataNew as MutableLiveData)
    }

    fun loadMoreMovie() {
        currentPage += 1
        loadMovieNew(loadMoreMovieDataNew as MutableLiveData)
    }

    fun stop() {
        val currentJob = job
        if (currentJob != null && currentJob.isActive) {
            currentJob.cancel()
        }
    }
}