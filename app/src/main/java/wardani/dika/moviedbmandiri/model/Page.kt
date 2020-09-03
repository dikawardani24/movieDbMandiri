package wardani.dika.moviedbmandiri.model

data class Page<T>(
        val page: Int,
        val totalPages: Int,
        val totalResults: Int,
        val content: List<T>
)