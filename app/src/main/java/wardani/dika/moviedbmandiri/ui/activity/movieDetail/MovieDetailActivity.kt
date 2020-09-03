@file:Suppress("DEPRECATION")

package wardani.dika.moviedbmandiri.ui.activity.movieDetail

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.api.ApiFactory
import wardani.dika.moviedbmandiri.databinding.ActivityMovieDetailBinding
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.model.Review
import wardani.dika.moviedbmandiri.model.Video
import wardani.dika.moviedbmandiri.repository.RepositoryFactory
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.ui.adapter.ItemReviewAdapter
import wardani.dika.moviedbmandiri.ui.adapter.ItemVideoAdapter
import wardani.dika.moviedbmandiri.ui.adapter.PagingAdapter
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.ui.listener.ScrollListener
import wardani.dika.moviedbmandiri.util.DateFormatterHelper
import wardani.dika.moviedbmandiri.util.showWarning
import wardani.dika.moviedbmandiri.util.updateAndroidSecurityProvider
import java.math.BigInteger
import java.util.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var videoAdapter: ItemVideoAdapter
    private lateinit var reviewAdapter: ItemReviewAdapter
    private lateinit var scrollListener: ScrollListener
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var loadingDetail: ProgressDialog

    private fun showView(show: Boolean, view: View) {
        view.visibility = when (show) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun showDataMovie(movie: Movie?) {
        val none = getString(R.string.none)
        binding.run {
            movieDetailContainer.run {
                val imageUrl = movie?.movieImage?.posterPath
                Picasso.get().load(imageUrl)
                    .into(imgCover)
                Picasso.get().load(imageUrl)
                    .into(imgPhoto)

                tvTitle.text = movie?.title ?: none
                tvName.text = movie?.title ?: none
                languageTv.text = movie?.originalLanguage ?: none

                val releaseDate = movie?.releaseDate
                movieReleaseDate.text =
                    if (releaseDate != null) DateFormatterHelper.format(releaseDate) else none

                val voteAverage = movie?.vote?.average
                ratingBar.rating = if (voteAverage != null) (voteAverage / 2).toFloat() else 0F

                originalTitleTv.text = movie?.originalTitle ?: none
                spokenLanguageTv.text = movie?.spokenLanguages?.joinToString { it.name } ?: none
                genreTv.text = movie?.genres?.joinToString { it.name } ?: none
                targetTv.text = movie?.movieTarget?.value ?: none
                imdbIdTv.text = movie?.imdbId ?: none
                homePageTv.text = movie?.homePage ?: none
                popularityTv.text = movie?.popularity?.toString() ?: none
                voteAverageTv.text = voteAverage?.toString() ?: none
                voteCountTv.text = movie?.vote?.count?.toString() ?: none
                budgetTv.text = movie?.budget?.toString() ?: none
                revenueTv.text = movie?.revenue?.toString() ?: none
                tvOverview.text = movie?.overview ?: none
            }
        }
    }

    private fun showNoDataDetailView(show: Boolean) {
        if (show) {
            showDataMovie(null)
        }
    }

    private fun showLoadingDetail(show: Boolean) {
        if (show) loadingDetail.show()
        else loadingDetail.hide()
    }

    private fun showDataViewForReview(show: Boolean) {
        val dataView = binding.movieReviewContainer.reviewRv
        val noDataView = binding.movieReviewContainer.noDataContainer.root
        showView(show, dataView)
        showView(!show, noDataView)
    }

    private fun showLoadingReview(show: Boolean) {
        val loadingView = binding.movieReviewContainer.loadingDataContainer.root
        showView(show, loadingView)
    }

    private fun showDataViewForVideos(show: Boolean) {
        val dataView = binding.movieVideosContainer.rvTrailer
        val noDataView = binding.movieVideosContainer.noDataContainer.root
        showView(show, dataView)
        showView(!show, noDataView)
    }

    private fun showLoadingVideos(show: Boolean) {
        val loadingView = binding.movieVideosContainer.loadingDataContainer.root
        showView(show, loadingView)
    }

    private fun handleReviewState(state: State<List<Review>>) {
        when (state) {
            is State.Loading -> showLoadingReview(true)
            is State.EmptyDataFetched -> {
                showLoadingReview(false)
                showDataViewForReview(false)
            }
            is State.DataFetched -> {
                showLoadingReview(false)
                showDataViewForReview(true)
                reviewAdapter.changeData(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingReview(false)
                showDataViewForReview(false)
                showWarning(state.errorMessage)
            }
        }
    }

    private fun handleLoadMoreReviewState(state: State<List<Review>>) {
        when (state) {
            is State.Loading -> {
                scrollListener.isLoading = true
                reviewAdapter.showLoading()
            }
            is State.EmptyDataFetched -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                reviewAdapter.closeLoading()
            }
            is State.DataFetched -> {
                scrollListener.isLoading = false
                reviewAdapter.closeLoading()
                reviewAdapter.changeData(state.data)
            }
            is State.ErrorOccurred -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                reviewAdapter.closeLoading()
                showWarning(state.errorMessage)
            }
        }
    }

    private fun handleVideoState(state: State<List<Video>>) {
        when (state) {
            is State.Loading -> showLoadingVideos(true)
            is State.EmptyDataFetched -> {
                showLoadingVideos(false)
                showDataViewForVideos(false)
            }
            is State.DataFetched -> {
                showLoadingVideos(false)
                showDataViewForVideos(true)
                videoAdapter.changeItems(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingVideos(false)
                showDataViewForVideos(false)
                showWarning(state.errorMessage)
            }
        }
    }

    private fun handleDetailData(state: State<Movie>) {
        when (state) {
            is State.Loading -> showLoadingDetail(true)
            is State.EmptyDataFetched -> {
                showLoadingDetail(false)
                showNoDataDetailView(true)
            }
            is State.DataFetched -> {
                showLoadingDetail(false)
                showNoDataDetailView(false)
                showDataMovie(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingDetail(false)
                showNoDataDetailView(true)
                showWarning(state.errorMessage)
            }
        }
    }

    private fun initViewModel() {
        val receivedData = intent.getSerializableExtra(MOVIE_KEY)
        val movieId = if (receivedData is BigInteger) receivedData else null

        val api = ApiFactory.createMovieDbApi(this)
        val repository = RepositoryFactory.createMovieRepository(api)
        viewModel = MovieDetailViewModel(
            application = application,
            movieRepository = repository,
            movieId = movieId
        )
    }

    private fun initView() {
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
            setDisplayHomeAsUpEnabled(true)
        }

        loadingDetail = ProgressDialog(this)
        loadingDetail.setTitle("Loading Detail Movie")
        loadingDetail.setMessage("Please wait...")

        binding.movieReviewContainer.run {
            val layoutManagerReview = LinearLayoutManager(this@MovieDetailActivity)
            scrollListener =
                ScrollListener(layoutManagerReview, object : ScrollListener.OnLoadMoreListener {
                    override fun onLoadMoreItems() {
                        viewModel.loadMoreReviews()
                    }
                })
            reviewAdapter = ItemReviewAdapter()
            reviewAdapter.onRetryLoadMoreListener = object : PagingAdapter.OnRetryLoadMoreListener {
                override fun onRetryLoadMore() {
                    viewModel.reloadReviews()
                }
            }
            reviewRv.layoutManager = layoutManagerReview
            reviewRv.adapter = reviewAdapter
            noDataContainer.retryBtn.setOnClickListener { viewModel.reloadReviews() }
        }

        binding.movieVideosContainer.run {
            val layoutManagerVideos = LinearLayoutManager(this@MovieDetailActivity)
            videoAdapter = ItemVideoAdapter()
            videoAdapter.onItemAdapterClickedListener = object : OnItemAdapterClickedListener<Video> {
                override fun onItemAdapterClicked(item: Video) {
                    if (item.site.toUpperCase(Locale.ROOT) == "Youtube".toUpperCase(Locale.ROOT)) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://www.youtube.com/watch?v=${item.key}")
                        startActivity(intent)
                    } else {
                        showWarning("Showing video from ${item.site} is not supported")
                    }
                }
            }

            rvTrailer.adapter = videoAdapter
            rvTrailer.layoutManager = layoutManagerVideos
            noDataContainer.retryBtn.setOnClickListener { viewModel.reloadDataVideos() }
        }

        viewModel.run {
            loadMovieDetailLiveData.observe(this@MovieDetailActivity) { handleDetailData(it) }
            loadVideosLiveData.observe(this@MovieDetailActivity) { handleVideoState(it) }
            loadReviewLiveData.observe(this@MovieDetailActivity) { handleReviewState(it) }
            loadMoreReviewLiveData.observe(this@MovieDetailActivity) { handleLoadMoreReviewState(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                viewModel.stop()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        if (Build.VERSION.SDK_INT <= 19) {
            updateAndroidSecurityProvider()
        }

        initViewModel()
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDetailMovie()
    }

    companion object {
        const val MOVIE_KEY = "movie"
    }
}