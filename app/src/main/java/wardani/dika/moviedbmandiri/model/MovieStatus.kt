package wardani.dika.moviedbmandiri.model

enum class MovieStatus(val description: String) {
    RUMORED("Rumored"),
    PLANNED("Planned"),
    IN_PRODUCTION("In Production"),
    POST_PRODUCTION("Post Production"),
    RELEASED("Released"),
    CANCELED("Canceled"),
    UNKNOWN("Unknown");

    companion object {

        fun toMovieStatus(description: String): MovieStatus {
            var found = UNKNOWN
            for (movieStatus in values()) {
                if (movieStatus.description == description) {
                    found = movieStatus
                    break
                }
            }

            return found
        }
    }
}