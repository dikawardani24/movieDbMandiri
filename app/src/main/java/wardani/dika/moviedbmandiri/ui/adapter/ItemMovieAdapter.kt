package wardani.dika.moviedbmandiri.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemMovieBinding
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.MovieViewHolder
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.util.TypeWrapper

class ItemMovieAdapter: PagingAdapter<Movie>() {
    var onItemAdapterClickedListener: OnItemAdapterClickedListener<Movie>? = null

    override fun onCreateViewHolderData(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        val binding: ItemMovieBinding = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, value: Movie) {
        if (holder is MovieViewHolder) {
            holder.bind(value, onItemAdapterClickedListener)
        }
    }
}