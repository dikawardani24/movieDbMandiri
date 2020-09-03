package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.MovieDetailResponse

data class BelongToCollection(
        var id: Int,
        var name: String
) {
    companion object {
        fun from(movieItem: MovieDetailResponse): BelongToCollection? {
            return if (movieItem.belongsToCollection == null)
                null else BelongToCollection(id = movieItem.belongsToCollection.id, name = "")
        }
    }
}