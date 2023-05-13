// To parse the JSON, install Klaxon and do:
//
//   val movieMedia = MovieMedia.fromJson(jsonString)

package com.dowplay.dowplay

import com.beust.klaxon.*

private val klaxon = Klaxon()

data class MovieMedia (
    val title: String? = null,
    val subTitle: String? = null,
    val url: String? = null,

    @Json(name = "media_id")
    val mediaID: String? = null,

    @Json(name = "media_type")
    val mediaType: String? = null,

    @Json(name = "user_id")
    val userID: String? = null,

    @Json(name = "profile_id")
    val profileID: String? = null,

    val token: String? = null,

    @Json(name = "api_base_url")
    val apiBaseURL: String? = null,

    val lang: String? = null,
    @Json(name = "start_at")
    val startAt: Double? = null,
    val info: Info? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<MovieMedia>(json)
    }
}

data class Info (
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,

    @Json(name = "download_url")
    val downloadURL: String? = null,

    @Json(name = "hd_url")
    val hdURL: String? = null,

    @Json(name = "trailer_url")
    val trailerURL: Any? = null,

    @Json(name = "media_url")
    val mediaURL: String? = null,

    val duration: String? = null,
    val language: String? = null,
    val translation: String? = null,
    val year: String? = null,

    @Json(name = "cover_photo")
    val coverPhoto: String? = null,

    @Json(name = "poster_photo")
    val posterPhoto: String? = null,

    val tags: List<Tag>? = null,
    val actors: List<DirectorInfo>? = null,

    @Json(name = "director_info")
    val directorInfo: DirectorInfo? = null,

    @Json(name = "is_favourite")
    val isFavourite: Boolean? = null,

    @Json(name = "imdb_rating")
    val imdbRating: String? = null,

    @Json(name = "imdb_certificate")
    val imdbCertificate: Any? = null,

    val watching: Watching? = null,
///////////////////////////////
    val order: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "release_date")
    val releaseDate: String? = null,
)

data class DirectorInfo (
    val id: Int? = null,
    val name: String? = null,
    val image: String? = null
)

data class Tag (
    val id: Int? = null,
    val title: String? = null
)

data class Watching (
    @Json(name = "current_time")
    val currentTime: String? = null,

    val duration: String? = null,
    val title: String? = null,
    val order: Int? = null,
    val description: String? = null,

    @Json(name = "last_media_id")
    val lastMediaID: Int? = null,

    @Json(name = "1080_url")
    val the1080_URL: String? = null,

    @Json(name = "720_url")
    val the720_URL: String? = null,

    @Json(name = "continue_type")
    val continueType: Any? = null,

    @Json(name = "next_type")
    val nextType: Any? = null,

    @Json(name = "next_episode")
    val nextEpisode: Any? = null
)