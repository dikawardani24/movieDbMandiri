package wardani.dika.moviedbmandiri.ui.activity.movieList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.api.ApiFactory
import wardani.dika.moviedbmandiri.databinding.FragmentMovieListBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.repository.RepositoryFactory
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.ui.activity.movieDetail.MovieDetailFragmentArgs
import wardani.dika.moviedbmandiri.ui.adapter.ItemMovieAdapter
import wardani.dika.moviedbmandiri.ui.adapter.PagingAdapter
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.ui.listener.ScrollListener
import wardani.dika.moviedbmandiri.util.showWarning

class MovieListFragment : Fragment() {
    private lateinit var binding: FragmentMovieListBinding
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
                requireActivity().showWarning(it.errorMessage)
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
                requireActivity().showWarning(it.errorMessage)
            }
        }
    }

    private fun initViewModel(genre: Genre?) {
        val api = ApiFactory.createMovieDbApi(requireContext())
        val repository = RepositoryFactory.createMovieListRepository(api)
        viewModel = MovieListViewModel(requireActivity().application, repository, genre)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.noDataContainer.messageError.text = "No Data Movie Available"

        adapter = ItemMovieAdapter()
        adapter.onRetryLoadMoreListener = object : PagingAdapter.OnRetryLoadMoreListener {
            override fun onRetryLoadMore() {
                viewModel.loadMoreMovie()
            }
        }

        adapter.onItemAdapterClickedListener = object : OnItemAdapterClickedListener<Movie> {
            override fun onItemAdapterClicked(item: Movie) {
                val args = MovieDetailFragmentArgs(item.id).toBundle()
                findNavController().navigate(R.id.toDetailMovie, args)
            }
        }

        val dataRv = binding.dataRv
        dataRv.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        dataRv.layoutManager = layoutManager
        scrollListener = ScrollListener(layoutManager, object : ScrollListener.OnLoadMoreListener {
            override fun onLoadMoreItems() {
                viewModel.loadMoreMovie()
            }
        })

        dataRv.addOnScrollListener(scrollListener)

        viewModel.run {
            loadMovieDataNew.observe(this@MovieListFragment) {
                handleFirstLoadMovie(it)
            }
            loadMoreMovieDataNew.observe(this@MovieListFragment) {
                handleLoadMoreMovie(it)
            }
            binding.noDataContainer.retryBtn.setOnClickListener {
                loadDataMovie()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)

        val args: MovieListFragmentArgs by navArgs()
        val receivedGenre = args.genre

        requireActivity().title = receivedGenre.name

        initViewModel(receivedGenre)
        initView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDataMovie()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }
}