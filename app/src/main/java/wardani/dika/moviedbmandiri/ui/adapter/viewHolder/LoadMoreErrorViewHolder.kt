package wardani.dika.moviedbmandiri.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.databinding.ItemLoadMoreErrorBinding
import wardani.dika.moviedbmandiri.ui.adapter.PagingAdapter

class LoadMoreErrorViewHolder(private val binding: ItemLoadMoreErrorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onRetryLoadMoreListener: PagingAdapter.OnRetryLoadMoreListener) {
        binding.retryBtn.setOnClickListener {
            onRetryLoadMoreListener.onRetryLoadMore()
        }
    }
}