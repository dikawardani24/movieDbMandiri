package wardani.dika.moviedbmandiri.ui.activity.movieList

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.api.ApiFactory
import wardani.dika.moviedbmandiri.databinding.ActivityMovieListBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.repository.RepositoryFactory
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.ui.activity.movieDetail.MovieDetailActivity
import wardani.dika.moviedbmandiri.ui.adapter.ItemMovieAdapter
import wardani.dika.moviedbmandiri.ui.adapter.PagingAdapter
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.ui.listener.ScrollListener
import wardani.dika.moviedbmandiri.util.showWarning
import wardani.dika.moviedbmandiri.util.startActivity
import wardani.dika.moviedbmandiri.util.updateAndroidSecurityProvider

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: ItemMovieAdapter
    private lateinit var viewModel: MovieListViewModel
    private lateinit var scrollListener: ScrollListener

    private fun showView(show: Boolean, view: View) {
        view.visibility = when (show) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun showNoDataView(show: Boolean) {
        val noDataView = binding.noDataContainer.root
        showView(show, noDataView)
    }

    private fun showLoadingDataView(show: Boolean) {
        val loadingDataView = binding.loadingDataContainer.root
        showView(show, loadingDataView)
    }

    private fun showDataView(show: Boolean) {
        val dataView = binding.dataRv
        showView(show, dataView)
    }

    private fun handleLoadMoreMovie(it: State<List<Movie>>?) {
        when (it) {
            is State.Loading -> {
                scrollListener.isLastPage = false
                scrollListener.isLoading = true
                adapter.showLoading()
            }
            is State.EmptyDataFetched -> {
                scrollListener.isLoading = false
                adapter.closeLoading()

                scrollListener.isLastPage = true
                adapter.showNoMoreData()
            }
            is State.DataFetched -> {
                scrollListener.isLoading = false
                adapter.closeLoading()

                adapter.changeData(it.data)
            }
            is State.ErrorOccurred -> {
                scrollListener.isLoading = false
                adapter.closeLoading()

                scrollListener.isLastPage = true
                adapter.showError()
                showWarning(it.errorMessage)
            }
        }
    }

    private fun handleFirstLoadMovie(it: State<List<Movie>>?) {
        when (it) {
            is State.Loading -> {
                showNoDataView(false)
                showLoadingDataView(true)
            }
            is State.EmptyDataFetched -> {
                showLoadingDataView(false)
                showNoDataView(true)
                showDataView(false)
            }
            is State.DataFetched -> {
                showLoadingDataView(false)
                showDataView(true)
                showNoDataView(false)
                adapter.changeData(it.data)
            }
            is State.ErrorOccurred -> {
                showLoadingDataView(false)
                showNoDataView(true)
                showWarning(it.errorMessage)
            }
        }
    }

    private fun initViewModel(genre: Genre?) {
        val api = ApiFactory.createMovieDbApi(this)
        val repository = RepositoryFactory.createMovieRepository(api)
        viewModel = MovieListViewModel(application, repository, genre)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.noDataContainer.messageError.text = "No Data Movie Available"

        adapter = ItemMovieAdapter()
        adapter.onRetryLoadMoreListener = object : PagingAdapter.OnRetryLoadMoreListener {
            override fun onRetryLoadMore() {
                viewModel.loadMoreMovie()
            }
        }

        adapter.onItemAdapterClickedListener = object : OnItemAdapterClickedListener<Movie> {
            override fun onItemAdapterClicked(item: Movie) {
                startActivity(MovieDetailActivity::class) {
                    putExtra(MovieDetailActivity.MOVIE_KEY, item.id)
                }
            }
        }

        val dataRv = binding.dataRv
        dataRv.adapter = adapter
        val layoutManager = LinearLayoutManager(this@MovieListActivity)
        dataRv.layoutManager = layoutManager
        scrollListener = ScrollListener(layoutManager, object : ScrollListener.OnLoadMoreListener {
            override fun onLoadMoreItems() {
                viewModel.loadMoreMovie()
            }
        })

        dataRv.addOnScrollListener(scrollListener)

        viewModel.run {
            loadMovieDataNew.observe(this@MovieListActivity) {
                handleFirstLoadMovie(it)
            }
            loadMoreMovieDataNew.observe(this@MovieListActivity) {
                handleLoadMoreMovie(it)
            }
            binding.noDataContainer.retryBtn.setOnClickListener {
                loadDataMovie()
            }
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)

        if (Build.VERSION.SDK_INT <= 19) {
            updateAndroidSecurityProvider()
        }

        val receivedGenre: Genre? = when(val receivedData = intent.getSerializableExtra(KEY_GENRE)) {
            is Genre -> receivedData
            else -> null
        }

        title = receivedGenre?.name ?: getString(R.string.app_name)

        initViewModel(receivedGenre)
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDataMovie()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }

    companion object {
        const val KEY_GENRE = "genre"
    }
}