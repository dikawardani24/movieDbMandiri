package wardani.dika.moviedbmandiri.ui.activity.movieDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.model.Review
import wardani.dika.moviedbmandiri.model.Video
import wardani.dika.moviedbmandiri.repository.movieDetail.MovieDetailRepository
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.util.Result
import java.math.BigInteger

class MovieDetailViewModel(
    application: Application,
    private val movieId: BigInteger?,
    private val movieDetailRepository: MovieDetailRepository
): AndroidViewModel(application) {
    val loadMovieDetailLiveData: LiveData<State<Movie>> = MutableLiveData()
    val loadVideosLiveData: LiveData<State<List<Video>>> = MutableLiveData()
    val loadReviewLiveData: LiveData<State<List<Review>>> = MutableLiveData()
    val loadMoreReviewLiveData: LiveData<State<List<Review>>> = MutableLiveData()

    private var currentMovie: Movie? = null
    private var job: Job? = null
    private val currentReviews = arrayListOf<Review>()
    private var currentPageReview = 1

    private suspend fun loadVideos(movie: Movie?) {
        val liveData = loadVideosLiveData as MutableLiveData
        liveData.postValue(State.Loading())

       if (movie != null) {
           when(val result = movieDetailRepository.getVideos(movie)) {
               is Result.Success -> {
                   val videos = result.data

                   if (videos.isEmpty()) {
                       liveData.postValue(State.EmptyDataFetched())
                   } else {
                       liveData.postValue(State.DataFetched(videos))
                   }
               }
               is Result.Failed -> {
                   val error = result.throwable
                   error.printStackTrace()
                   liveData.postValue(State.ErrorOccurred(error.message ?: ""))
               }
           }
       } else {
           liveData.postValue(State.ErrorOccurred("No data movie has been loaded"))
       }
    }

    private suspend fun loadReviews(movie: Movie?, liveData: MutableLiveData<State<List<Review>>>) {

        if (movie != null) {
            when(val result = movieDetailRepository.getMovieReviews(movie, currentPageReview)) {
                is Result.Success -> {
                    val page = result.data
                    val reviews = page.content

                    if (reviews.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        currentReviews.addAll(reviews)
                        liveData.postValue(State.DataFetched(currentReviews))
                    }
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                    currentPageReview -= 1
                }
            }
        } else {
            liveData.postValue(State.ErrorOccurred("No data movie has been loaded"))
        }
    }

    fun reloadDataVideos() {
        job = viewModelScope.launch {
            loadVideos(currentMovie)
        }
    }

    fun reloadReviews() {
        currentReviews.clear()
        currentPageReview = 1

        job = viewModelScope.launch {
            loadReviews(currentMovie, loadReviewLiveData as MutableLiveData)
        }
    }

    fun loadMoreReviews() {
        currentPageReview += 1
        job = viewModelScope.launch {
            loadReviews(currentMovie, loadMoreReviewLiveData as MutableLiveData)
        }
    }

    fun loadDetailMovie() {
        val liveData = loadMovieDetailLiveData as MutableLiveData

        if (movieId != null) {
            liveData.postValue(State.Loading())
            job = viewModelScope.launch {
                when(val result = movieDetailRepository.getMovieDetail(movieId)) {
                    is Result.Success -> {
                        val movie = result.data
                        currentMovie = movie
                        liveData.postValue(State.DataFetched(movie))
                        loadVideos(movie)
                        loadReviews(movie, loadReviewLiveData as MutableLiveData)
                    }
                    is Result.Failed -> {
                        val error = result.throwable
                        error.printStackTrace()
                        liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                    }
                }
            }
        } else {
            liveData.postValue(State.ErrorOccurred("No data movie has been received, load detail movie is canceled"))
        }
    }

    fun stop() {
        val currentJob = job
        if (currentJob != null && currentJob.isActive) {
            currentJob.cancel()
        }
    }
}