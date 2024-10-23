package com.dowplay.dowplay


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
internal data class EpisodeMedia(
    @SerializedName("api_base_url")
    @Expose
    val apiBaseUrl: String?,
    @SerializedName("info")
    @Expose
    val info: Info?,
    @SerializedName("lang")
    @Expose
    val lang: String?,
    @SerializedName("media_group")
    @Expose
    val mediaGroup: MediaGroup?,
    @SerializedName("media_type")
    @Expose
    val mediaType: String?,
    @SerializedName("profile_id")
    @Expose
    val profileId: String?,
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("user_id")
    @Expose
    val userId: String?,
    @SerializedName("is_download_enabled")
    @Expose
    val isDownloadEnabled: Boolean?
) {
    @Keep
     data class Info(
        @SerializedName("created_at")
        @Expose
        val createdAt: String?,
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
        @SerializedName("media_url")
        @Expose
        val mediaUrl: String?,
        @SerializedName("order")
        @Expose
        val order: String?,
        @SerializedName("poster_photo")
        @Expose
        val posterPhoto: String?,
        @SerializedName("release_date")
        @Expose
        val releaseDate: String?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("trailer_url")
        @Expose
        val trailerUrl: String?,
        @SerializedName("watching")
        @Expose
        val watching: Watching?
    ) {
        @Keep
         data class Watching(
            @SerializedName("current_time")
            @Expose
            val currentTime: String?,
            @SerializedName("duration")
            @Expose
            val duration: String?
        )
    }

    @Keep
     data class MediaGroup(
        @SerializedName("episodes")
        @Expose
        val episodes: List<Episode?>?,
        @SerializedName("items_ids")
        @Expose
        val itemsIds: ItemsIds?,
        @SerializedName("season")
        @Expose
        val season: Season?,
        @SerializedName("tv_show")
        @Expose
        val tvShow: TvShow?
    ) {
        @Keep
         data class Episode(
            @SerializedName("created_at")
            @Expose
            val createdAt: String?,
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
            @SerializedName("media_url")
            @Expose
            val mediaUrl: String?,
            @SerializedName("order")
            @Expose
            val order: String?,
            @SerializedName("poster_photo")
            @Expose
            val posterPhoto: String?,
            @SerializedName("release_date")
            @Expose
            val releaseDate: String?,
            @SerializedName("title")
            @Expose
            val title: String?,
            @SerializedName("trailer_url")
            @Expose
            val trailerUrl: String?,
            @SerializedName("watching")
            @Expose
            val watching: Watching?
        ) {
            @Keep
             data class Watching(
                @SerializedName("current_time")
                @Expose
                val currentTime: String?,
                @SerializedName("duration")
                @Expose
                val duration: String?
            )
        }

        @Keep
         data class ItemsIds(
            @SerializedName("season_id")
            @Expose
            val seasonId: String?,
            @SerializedName("tv_show_id")
            @Expose
            val tvShowId: String?
        )

        @Keep
         data class Season(
            @SerializedName("cover_photo")
            @Expose
            val coverPhoto: String?,
            @SerializedName("id")
            @Expose
            val id: Int?,
            @SerializedName("poster_photo")
            @Expose
            val posterPhoto: String?,
            @SerializedName("season_id")
            @Expose
            val seasonId: Int?,
            @SerializedName("season_number")
            @Expose
            val seasonNumber: String?,
            @SerializedName("title")
            @Expose
            val title: String?,
            @SerializedName("trailer_url")
            @Expose
            val trailerUrl: String?
        )

        @Keep
         data class TvShow(
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
            @SerializedName("end_year")
            @Expose
            val endYear: String?,
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
            @SerializedName("last_watching")
            @Expose
            val lastWatching: LastWatching?,
            @SerializedName("last_watching_season_id")
            @Expose
            val lastWatchingSeasonId: Int?,
            @SerializedName("poster_photo")
            @Expose
            val posterPhoto: String?,
            @SerializedName("seasons")
            @Expose
            val seasons: List<Season?>?,
            @SerializedName("start_year")
            @Expose
            val startYear: String?,
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
            val translation: String?
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
             data class LastWatching(
                @SerializedName("continue_type")
                @Expose
                val continueType: String?,
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
                val nextEpisode: NextEpisode?,
                @SerializedName("next_type")
                @Expose
                val nextType: String?,
                @SerializedName("order")
                @Expose
                val order: String?,
                @SerializedName("title")
                @Expose
                val title: String?,
                @SerializedName("1080_url")
                @Expose
                val the1080_url: String?,
                @SerializedName("720_url")
                @Expose
                val the720_URL: String?
            ) {
                @Keep
                 data class NextEpisode(
                    @SerializedName("created_at")
                    @Expose
                    val createdAt: String?,
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
                    @SerializedName("media_url")
                    @Expose
                    val mediaUrl: String?,
                    @SerializedName("order")
                    @Expose
                    val order: String?,
                    @SerializedName("poster_photo")
                    @Expose
                    val posterPhoto: String?,
                    @SerializedName("release_date")
                    @Expose
                    val releaseDate: String?,
                    @SerializedName("title")
                    @Expose
                    val title: String?,
                    @SerializedName("trailer_url")
                    @Expose
                    val trailerUrl: String?,
                    @SerializedName("watching")
                    @Expose
                    val watching: Watching?
                ) {
                    @Keep
                     data class Watching(
                        @SerializedName("current_time")
                        @Expose
                        val currentTime: String?,
                        @SerializedName("duration")
                        @Expose
                        val duration: String?
                    )
                }
            }

            @Keep
            internal data class Season(
                @SerializedName("cover_photo")
                @Expose
                val coverPhoto: String?,
                @SerializedName("id")
                @Expose
                val id: Int?,
                @SerializedName("poster_photo")
                @Expose
                val posterPhoto: String?,
                @SerializedName("season_id")
                @Expose
                val seasonId: Int?,
                @SerializedName("season_number")
                @Expose
                val seasonNumber: String?,
                @SerializedName("title")
                @Expose
                val title: String?,
                @SerializedName("trailer_url")
                @Expose
                val trailerUrl: String?
            )

            @Keep
            internal data class Tag(
                @SerializedName("id")
                @Expose
                val id: Int?,
                @SerializedName("title")
                @Expose
                val title: String?
            )
        }
    }
}