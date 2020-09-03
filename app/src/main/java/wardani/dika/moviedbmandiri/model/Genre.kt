package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.GenreResponse
import java.io.Serializable

data class Genre(
        var id: Int,
        var name: String
): Serializable {
    companion object {
        fun from(genreId: Int): Genre {
            return Genre(
                    id = genreId,
                    name = ""
            )
        }

        fun from(genre: GenreResponse): Genre {
            return Genre(
                    id = genre.id,
                    name = genre.name
            )
        }
    }
}