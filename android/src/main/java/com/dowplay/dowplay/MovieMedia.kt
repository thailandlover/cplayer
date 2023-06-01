package com.dowplay.dowplay


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class MovieMedia(
    @SerializedName("api_base_url")
    @Expose
    val apiBaseUrl: String?,
    @SerializedName("info")
    @Expose
    val info: Info?,
    @SerializedName("lang")
    @Expose
    val lang: String?,
    @SerializedName("media_id")
    @Expose
    val mediaId: String?,
    @SerializedName("media_type")
    @Expose
    val mediaType: String?,
    @SerializedName("profile_id")
    @Expose
    val profileId: String?,
    @SerializedName("start_at")
    @Expose
    val startAt: Int?,
    @SerializedName("sub_title")
    @Expose
    val subTitle: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("url")
    @Expose
    val url: String?,
    @SerializedName("user_id")
    @Expose
    val userId: String?
) {
    @Keep
    data class Info(
        @SerializedName("actors")
        @Expose
        val actors: List<Actor?>?,
        @SerializedName("cover_photo")
        @Expose
        val coverPhoto: String?,
        @SerializedName("description")
        @Expose
        val description: String?,
        @SerializedName("director_info")
        @Expose
        val directorInfo: DirectorInfo?,
        @SerializedName("download_url")
        @Expose
        val downloadUrl: String?,
        @SerializedName("duration")
        @Expose
        val duration: String?,
        @SerializedName("hd_url")
        @Expose
        val hdUrl: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("imdb_certificate")
        @Expose
        val imdbCertificate: String?,
        @SerializedName("imdb_rating")
        @Expose
        val imdbRating: String?,
        @SerializedName("is_favourite")
        @Expose
        val isFavourite: Boolean?,
        @SerializedName("language")
        @Expose
        val language: String?,
        @SerializedName("media_url")
        @Expose
        val mediaUrl: String?,
        @SerializedName("poster_photo")
        @Expose
        val posterPhoto: String?,
        @SerializedName("tags")
        @Expose
        val tags: List<Tag?>?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("trailer_url")
        @Expose
        val trailerUrl: String?,
        @SerializedName("translation")
        @Expose
        val translation: String?,
        @SerializedName("watching")
        @Expose
        val watching: Watching?,
        @SerializedName("year")
        @Expose
        val year: String?
    ) {
        @Keep
        data class Actor(
            @SerializedName("id")
            @Expose
            val id: Int?,
            @SerializedName("image")
            @Expose
            val image: String?,
            @SerializedName("name")
            @Expose
            val name: String?
        )

        @Keep
        data class DirectorInfo(
            @SerializedName("id")
            @Expose
            val id: Int?,
            @SerializedName("image")
            @Expose
            val image: String?,
            @SerializedName("name")
            @Expose
            val name: String?
        )

        @Keep
        data class Tag(
            @SerializedName("id")
            @Expose
            val id: Int?,
            @SerializedName("title")
            @Expose
            val title: String?
        )

        @Keep
        data class Watching(
            @SerializedName("continue_type")
            @Expose
            val continueType: Any?,
            @SerializedName("current_time")
            @Expose
            val currentTime: String?,
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("duration")
            @Expose
            val duration: String?,
            @SerializedName("last_media_id")
            @Expose
            val lastMediaId: Int?,
            @SerializedName("next_episode")
            @Expose
            val nextEpisode: Any?,
            @SerializedName("next_type")
            @Expose
            val nextType: Any?,
            @SerializedName("order")
            @Expose
            val order: Int?,
            @SerializedName("title")
            @Expose
            val title: String?,
            @SerializedName("1080_url")
            @Expose
            val url8: String?,
            @SerializedName("720_url")
            @Expose
            val url7: String?
        )
    }
}