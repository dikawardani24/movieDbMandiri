package wardani.dika.moviedbmandiri.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.databinding.ItemReviewBinding
import wardani.dika.moviedbmandiri.model.Review
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener

class ReviewViewHolder(private val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(review: Review, onItemAdapterClickedListener: OnItemAdapterClickedListener<Review>?) {
        binding.run {
            authorTv.text = review.author
            contentTv.text = review.content

            if (onItemAdapterClickedListener != null) {
                openPageBtn.setOnClickListener {
                    onItemAdapterClickedListener.onItemAdapterClicked(review)
                }
            }
        }
    }
}