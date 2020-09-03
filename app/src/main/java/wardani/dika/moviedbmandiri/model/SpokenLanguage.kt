package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.SpokenLanguageResponse

data class SpokenLanguage(
        var iso6391: String,
        var name: String
) {
    companion object {
        fun from(spokenLanguageResponse: SpokenLanguageResponse): SpokenLanguage {
            return SpokenLanguage(
                    iso6391 = spokenLanguageResponse.iso6391,
                    name = spokenLanguageResponse.name
            )
        }
    }
}