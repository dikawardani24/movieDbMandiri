package wardani.dika.moviedbmandiri.api.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse (
	@SerializedName("author")
	val author : String,
	@SerializedName("content")
	val content : String,
	@SerializedName("id")
	val id : String,
	@SerializedName("url")
	val url : String
)