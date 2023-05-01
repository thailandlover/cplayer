package com.dowplay.dowplay

// To parse the JSON, install Klaxon and do:
//
//   val episodeMedia = EpisodeMedia.fromJson(jsonString)
import com.beust.klaxon.*

private val klaxon = Klaxon()

data class EpisodeMedia (
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
    val info: Info? = null,

    @Json(name = "media_group")
    val mediaGroup: MediaGroup? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<EpisodeMedia>(json)
    }
}


data class MediaGroup (
    @Json(name = "tv_show")
    val tvShow: TvShow? = null,

    val season: Season? = null,

    @Json(name = "items_ids")
    val itemsIDS: ItemsIDS? = null,

    val episodes: List<Info>? = null
)

data class ItemsIDS (
    @Json(name = "season_id")
    val seasonID: String? = null,

    @Json(name = "tv_show_id")
    val tvShowID: String? = null
)

data class Season (
    val id: Long? = null,

    @Json(name = "season_id")
    val seasonID: Long? = null,

    @Json(name = "poster_photo")
    val posterPhoto: String? = null,

    @Json(name = "cover_photo")
    val coverPhoto: Any? = null,

    @Json(name = "trailer_url")
    val trailerURL: Any? = null,

    @Json(name = "season_number")
    val seasonNumber: String? = null,

    val title: String? = null
)

data class TvShow (
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,

    @Json(name = "trailer_url")
    val trailerURL: Any? = null,

    val language: String? = null,
    val translation: String? = null,

    @Json(name = "start_year")
    val startYear: String? = null,

    @Json(name = "end_year")
    val endYear: String? = null,

    @Json(name = "cover_photo")
    val coverPhoto: Any? = null,

    @Json(name = "poster_photo")
    val posterPhoto: String? = null,

    val seasons: List<Season>? = null,
    val tags: List<Tag>? = null,
    val actors: List<Any?>? = null,

    @Json(name = "director_info")
    val directorInfo: Any? = null,

    @Json(name = "is_favourite")
    val isFavourite: Boolean? = null,

    @Json(name = "imdb_rating")
    val imdbRating: Any? = null,

    @Json(name = "imdb_certificate")
    val imdbCertificate: Any? = null,

    @Json(name = "last_watching")
    val lastWatching: Any? = null,

    @Json(name = "last_watching_season_id")
    val lastWatchingSeasonID: Any? = null
)