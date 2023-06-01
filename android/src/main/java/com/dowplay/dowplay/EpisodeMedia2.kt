/*package com.dowplay.dowplay

// To parse the JSON, install Klaxon and do:
//
//   val episodeMedia = EpisodeMedia.fromJson(jsonString)
import com.beust.klaxon.*

private val klaxon = Klaxon()

data class EpisodeMedia(
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
    val info: MovieMedia.Info? = null,
    @Json(name = "media_group")
    val mediaGroup: MediaGroup? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<EpisodeMedia>(json)
    }
}

data class Watching(
    @Json(name = "current_time")
    val currentTime: String? = null,

    val duration: String? = null
)

data class MediaGroup(
    @Json(name = "tv_show")
    val tvShow: TvShow? = null,

    val season: Season? = null,

    @Json(name = "items_ids")
    val itemsIDS: ItemsIDS? = null,

    val episodes: List<MovieMedia.Info>? = null
)

data class ItemsIDS(
    @Json(name = "season_id")
    val seasonID: String? = null,

    @Json(name = "tv_show_id")
    val tvShowID: String? = null
)

data class Season(
    val id: Int? = null,

    @Json(name = "season_id")
    val seasonID: Int? = null,

    @Json(name = "poster_photo")
    val posterPhoto: String? = null,

    @Json(name = "cover_photo")
    val coverPhoto: String? = null,

    @Json(name = "trailer_url")
    val trailerURL: String? = null,

    @Json(name = "season_number")
    val seasonNumber: String? = null,

    val title: String? = null
)

data class TvShow(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,

    @Json(name = "trailer_url")
    val trailerURL: String? = null,

    val language: String? = null,
    val translation: String? = null,

    @Json(name = "start_year")
    val startYear: String? = null,

    @Json(name = "end_year")
    val endYear: String? = null,

    @Json(name = "cover_photo")
    val coverPhoto: String? = null,

    @Json(name = "poster_photo")
    val posterPhoto: String? = null,

    val seasons: List<Season>? = null,
    val tags: List<Tag>? = null,
    val actors: List<Actor>? = null,

    @Json(name = "director_info")
    val directorInfo: MovieMedia.Info.DirectorInfo? = null,

    @Json(name = "is_favourite")
    val isFavourite: Boolean? = null,

    @Json(name = "imdb_rating")
    val imdbRating: String? = null,

    @Json(name = "imdb_certificate")
    val imdbCertificate: String? = null,

    @Json(name = "last_watching")
    val lastWatching: LastWatching? = null,

    @Json(name = "last_watching_season_id")
    val lastWatchingSeasonID: Int? = null
)

data class Actor(
    val id: Int? = null,
    val name: String? = null,
    val image: String? = null
)

data class LastWatching(
    @Json(name = "current_time")
    val currentTime: String? = null,

    val duration: String? = null,
    val title: String? = null,
    val order: String? = null,
    val description: String? = null,

    @Json(name = "last_media_id")
    val lastMediaID: Int? = null,

    @Json(name = "1080_url")
    val the1080_URL: String? = null,

    @Json(name = "720_url")
    val the720_URL: String? = null,

    @Json(name = "continue_type")
    val continueType: String? = null,

    @Json(name = "next_type")
    val nextType: String? = null,

    @Json(name = "next_episode")
    val nextEpisode: MovieMedia.Info? = null
)

data class Tag(
    val id: Int? = null,
    val title: String? = null
)
*/