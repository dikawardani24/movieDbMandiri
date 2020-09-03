package wardani.dika.moviedbmandiri.ui.adapter.viewHolder

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import wardani.dika.moviedbmandiri.R
import wardani.dika.moviedbmandiri.databinding.ItemMovieBinding
import wardani.dika.moviedbmandiri.model.Movie
import wardani.dika.moviedbmandiri.ui.listener.OnItemAdapterClickedListener
import wardani.dika.moviedbmandiri.util.DateFormatterHelper

class MovieViewHolder (private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, onItemAdapterClickedListener: OnItemAdapterClickedListener<Movie>?) {
        binding.run {
            movieTitle.text = movie.title
            movieReleaseDate.text = DateFormatterHelper.format(movie.releaseDate)
            voteAverageRb.rating = (movie.vote.average / 2).toFloat()
            descTv.text = movie.overview
            languageTv.text = movie.originalLanguage

            if (onItemAdapterClickedListener != null) {
                root.setOnClickListener {
                    onItemAdapterClickedListener.onItemAdapterClicked(movie)
                }
            }

            Picasso.get().load(movie.movieImage?.posterPath)
                .into(movieImg, object : Callback {
                    override fun onSuccess() {
                        movieImg.scaleType = ImageView.ScaleType.FIT_CENTER
                    }

                    override fun onError(e: Exception?) {
                        movieImg.run {
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                            setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_baseline_broken_image_24))
                        }
                    }
                })
        }
    }
}