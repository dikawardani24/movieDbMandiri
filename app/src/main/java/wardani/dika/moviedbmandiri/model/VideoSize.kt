package wardani.dika.moviedbmandiri.model

enum class VideoSize(val value: Int) {
    V_360(360),
    V_480(480),
    V_720(720),
    V_1080(1080),
    UNKNOWN(0);

    companion object {
        fun toVideoSize(size: Int): VideoSize {
            var found = UNKNOWN
            for (videoSize in values()) {
                if (videoSize.value == size) {
                    found = videoSize
                    break
                }
            }

            return found
        }
    }
}