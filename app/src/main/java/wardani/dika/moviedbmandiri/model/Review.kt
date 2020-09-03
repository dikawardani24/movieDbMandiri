package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.ReviewResponse

data class Review(
    var id: String,
    var author: String,
    var content: String,
    var url: String ,
    var movie: Movie
) {
    companion object {
        fun from(reviewResponse: ReviewResponse, movie: Movie): Review {
            return Review(
                id = reviewResponse.id,
                author = reviewResponse.author,
                content = reviewResponse.content,
                url = reviewResponse.url,
                movie = movie
            )
        }
    }
}