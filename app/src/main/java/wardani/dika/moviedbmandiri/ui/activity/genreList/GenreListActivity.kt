package wardani.dika.moviedbmandiri.ui.activity.genreList

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.api.ApiFactory
import wardani.dika.moviedbmandiri.databinding.ActivityGenreListBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.repository.RepositoryFactory
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.ui.activity.movieList.MovieListActivity
import wardani.dika.moviedbmandiri.ui.adapter.ItemGenreAdapter
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.util.showWarning
import wardani.dika.moviedbmandiri.util.startActivity
import wardani.dika.moviedbmandiri.util.updateAndroidSecurityProvider

class GenreListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenreListBinding
    private lateinit var adapter: ItemGenreAdapter
    private lateinit var viewModel: GenreListViewModel

    private fun showView(show: Boolean, view: View) {
        view.visibility = when (show) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun showDataView(show: Boolean) {
        val dataView = binding.genreRv
        showView(show, dataView)
    }

    private fun showNoDataView(show: Boolean) {
        val noDataView = binding.noDataContainer.root
        showView(show, noDataView)
    }

    private fun showLoadingDataView(show: Boolean) {
        val loadingDataView = binding.loadingDataContainer.root
        showView(show, loadingDataView)
    }

    private fun handleLoadGenre(it: State<List<Genre>>) {
        when(it) {
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
                adapter.changeItems(it.data)
                Log.d(TAG, "Data fetched")
            }
            is State.ErrorOccurred -> {
                showLoadingDataView(false)
                showNoDataView(true)
                showDataView(false)
                showWarning(it.errorMessage)
            }
        }
    }

    private fun initViewModel() {
        val api = ApiFactory.createMovieDbApi(this)
        val repository = RepositoryFactory.createMovieRepository(api)
        viewModel = GenreListViewModel(application, repository)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.noDataContainer.messageError.text = "No Data Genre Available"

        adapter = ItemGenreAdapter()
        adapter.onItemAdapterClickedListener = object : OnItemAdapterClickedListener<Genre> {
            override fun onItemAdapterClicked(item: Genre) {
                startActivity(MovieListActivity::class) {
                    putExtra(MovieListActivity.KEY_GENRE, item)
                }
            }
        }

        val dataView = binding.genreRv
        dataView.adapter = adapter
        val layoutManager = GridLayoutManager(this, 3)
        dataView.layoutManager = layoutManager
    }

    private fun initListener() {
        viewModel.run {
            genreListLiveData.observe(this@GenreListActivity) { handleLoadGenre(it) }
            binding.noDataContainer.retryBtn.setOnClickListener { loadDataGenre() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_genre_list)

        if (Build.VERSION.SDK_INT <= 19) {
            updateAndroidSecurityProvider()
        }

        initViewModel()
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDataGenre()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }

    companion object {
        private const val TAG = "GenreListActivity"
    }
}