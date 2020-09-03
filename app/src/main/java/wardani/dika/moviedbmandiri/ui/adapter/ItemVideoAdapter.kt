package wardani.dika.moviedbmandiri.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemVideoBinding
import wardani.dika.moviedbmandiri.model.Video
import wardani.dika.moviedbmandiri.ui.adapter.viewHolder.VideoViewHolder
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener

class ItemVideoAdapter : StaticAdapter<VideoViewHolder, Video>() {
    var onItemAdapterClickedListener: OnItemAdapterClickedListener<Video>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemVideoBinding = DataBindingUtil.inflate(inflater, R.layout.item_video, parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position], onItemAdapterClickedListener)
    }

}