package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status_code")
    var statusCode: Int,
    @SerializedName("status_message")
    var statusMessage: String
)