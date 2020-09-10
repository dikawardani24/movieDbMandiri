package wardani.dika.moviedbmandiri.ui.activity.genreList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.api.ApiFactory
import wardani.dika.moviedbmandiri.databinding.FragmentGenreListBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.repository.RepositoryFactory
import wardani.dika.moviedbmandiri.ui.State
import wardani.dika.moviedbmandiri.ui.activity.movieList.MovieListFragmentArgs
import wardani.dika.moviedbmandiri.ui.adapter.ItemGenreAdapter
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.util.showWarning

class GenreListFragment : Fragment() {
    private lateinit var binding: FragmentGenreListBinding
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
                requireActivity().showWarning(it.errorMessage)
            }
        }
    }

    private fun initViewModel() {
        val api = ApiFactory.createMovieDbApi(requireContext())
        val repository = RepositoryFactory.createGenreRepository(api)
        viewModel = GenreListViewModel(requireActivity().application, repository)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.noDataContainer.messageError.text = "No Data Genre Available"

        adapter = ItemGenreAdapter()
        adapter.onItemAdapterClickedListener = object : OnItemAdapterClickedListener<Genre> {
            override fun onItemAdapterClicked(item: Genre) {
                val args = MovieListFragmentArgs(item).toBundle()
                findNavController().navigate(R.id.toMovieList, args)
            }
        }

        val dataView = binding.genreRv
        dataView.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        dataView.layoutManager = layoutManager
    }

    private fun initListener() {
        viewModel.run {
            genreListLiveData.observe(this@GenreListFragment) { handleLoadGenre(it) }
            binding.noDataContainer.retryBtn.setOnClickListener { loadDataGenre() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_list, container, false)

        initViewModel()
        initView()
        initListener()

        return binding.root
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