package wardani.dika.moviedbmandiri.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemReviewBinding
import wardani.dika.moviedbmandiri.model.Review
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.ReviewViewHolder
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.util.TypeWrapper

class ItemReviewAdapter: PagingAdapter<Review>() {
    var onItemAdapterClickedListener: OnItemAdapterClickedListener<Review>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, value: Review) {
        if (holder is ReviewViewHolder) {
            holder.bind(value, onItemAdapterClickedListener)
        }
    }

    override fun onCreateViewHolderData(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val binding: ItemReviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_review, parent, false)
        return ReviewViewHolder(binding)
    }

}