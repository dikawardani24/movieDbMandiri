package wardani.dika.moviedbmandiri.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import wardani.dika.moviedbmandiri.api.response.GenreResponse
import java.io.Serializable

@Parcelize
data class Genre(
        var id: Int,
        var name: String
): Parcelable {
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