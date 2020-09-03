package wardani.dika.moviedbmandiri.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.databinding.ItemGenreBinding
import wardani.dika.moviedbmandiri.model.Genre
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener

class GenreViewHolder(private val binding: ItemGenreBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(genre: Genre, onItemAdapterClickedListener: OnItemAdapterClickedListener<Genre>?) {
        binding.run {
            menuTitle.text = genre.name
            if (onItemAdapterClickedListener != null) {
                root.setOnClickListener {
                    onItemAdapterClickedListener.onItemAdapterClicked(genre)
                }
            }
        }
    }

}