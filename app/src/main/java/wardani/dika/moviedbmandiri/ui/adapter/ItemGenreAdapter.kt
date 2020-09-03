package wardani.dika.moviedbmandiri.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemGenreBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.GenreViewHolder
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener

class ItemGenreAdapter: StaticAdapter<GenreViewHolder, Genre>() {
    var onItemAdapterClickedListener: OnItemAdapterClickedListener<Genre>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val binding: ItemGenreBinding = DataBindingUtil.inflate(inflater, R.layout.item_genre, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(items[position], onItemAdapterClickedListener)
    }

}