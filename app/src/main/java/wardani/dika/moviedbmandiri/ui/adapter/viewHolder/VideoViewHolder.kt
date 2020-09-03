package wardani.dika.moviedbmandiri.ui.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.moviedbmandiri.databinding.ItemVideoBinding
import wardani.dika.moviedbmandiri.model.Video
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener

class VideoViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(video: Video, onItemAdapterClickedListener: OnItemAdapterClickedListener<Video>?) {

        binding.run {
            nameTv.text = video.name
            typeTv.text = video.type.value
            sourceTv.text = video.site
            sizeTv.text = "${video.size.value} p"

            if (onItemAdapterClickedListener != null) {
                root.setOnClickListener {
                    onItemAdapterClickedListener.onItemAdapterClicked(video)
                }
            }
        }
    }
}