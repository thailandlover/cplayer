package com.dowplay.dowplay

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.downloader.PRDownloader
import com.downloader.Status
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import java.io.File
import java.math.BigInteger
import java.security.SecureRandom


class DownloaderDowPlay(initContext: Context, initActivity: Activity) : ActivityAware {

    private var context: Context
    private var lactivity: Activity

    init {
        context = initContext
        lactivity = initActivity
    }

    fun generateRandomToken(length: Int): String {
        val secureRandom = SecureRandom()
        val token = BigInteger(130, secureRandom).toString(32)
        return token.take(length)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
////FOR Permission
    var myRequestCode = 1997
    var permissionToDownload = false

    private fun showDownloadStatePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    lactivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    lactivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                if ("ar".equals("en", ignoreCase = true)) {
                    showExplanation(
                        "Permission Needed",
                        "Media and file access must be granted to start downloading movies and series through the app permissions option",
                        true
                    )
                } else {
                    showExplanation(
                        "منح الإذن",
                        "يجب منح صلاحية الوصول للوسائط والملفات لبدء تحميل الافلام والمسلسلات من خلال خيار اذونات التطبيق",
                        true
                    )
                }
            } else {
                if ("ar".equals("en", ignoreCase = true)) {
                    showExplanation(
                        "Permission Needed",
                        "You must grant access to media and files to start downloading movies and series",
                        false
                    )
                } else {
                    showExplanation(
                        "منح الإذن",
                        "يجب منح صلاحية الوصول للوسائط والملفات لبدء تحميل الافلام والمسلسلات",
                        false
                    )
                }
            }
        } else {
            permissionToDownload = true
        }
    }

    private fun showExplanation(title: String, message: String, openSettingScreen: Boolean) {
        val builder = AlertDialog.Builder(lactivity, R.style.ScreenDialogTheme)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, id ->
                if (openSettingScreen) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.data = uri
                    context.startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(
                        lactivity,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        myRequestCode
                    )
                }
            }
        builder.create().show()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    fun startDownload(
        url: String,
        name: String,
        mediaType: String,
        mediaId: String,
        mediaData: String,
        userId: String,
        profileId: String,
        seasonId: String,
        episodeId: String,
        seasonOrder: String,
        episodeOrder: String,
        seasonName: String,
        episodeName: String
    ): Int {
        showDownloadStatePermission()
        if (permissionToDownload) {
            //PRDownloader.initialize(context);
            // Enabling database for resume support even after the application is killed:
            //val url1 = "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4"
            //var url1 = "https://thekee.gcdn.co/video/m-159n/English/Drama/Deathwatch.2002.1080p.V1.mp4"
            //for save in public pkg app
            //var dirPath = "${context.getExternalFilesDir(null)}"
            //for save in private pkg app

            val downloadInfo = DatabaseHelper(context).getDownloadInfoFromDB(mediaId, mediaType)
            val status: Status = PRDownloader.getStatus(downloadInfo["download_id"].toString().toIntOrNull()?:-1)

            Log.d(
                "startDownload Method",
                "this video is downloaded..." + mediaId + " > " + mediaType + " > " + downloadInfo["status"]
            )
            if (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_SUCCESSFUL || status == Status.RUNNING) {
                Log.d("startDownload Method", "this video is downloaded...")
                return 0
            } else {
                if ((downloadInfo["status"] == DownloadManagerSTATUS.STATUS_RUNNING && status != Status.RUNNING)
                    || (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_PAUSED && status != Status.PAUSED)
                    || (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_FAILED)
                    || status == Status.FAILED
                    || status == Status.CANCELLED
                    || status == Status.UNKNOWN
                ) {
                    cancelDownload(mediaId, mediaType)
                }
                val dirPath = context.filesDir.path + "/downplay"
                val videoName = generateRandomToken(50) + ".mp4"
                println("Bom Dir: $dirPath")
                println("Bom Dir: ${"$dirPath/$videoName"}")
                val intent = Intent(context, DownloadService::class.java).apply {
                    putExtra("url", url)
                    putExtra("dir_path", dirPath)
                    putExtra("video_name", videoName)
                    putExtra("full_path", "$dirPath/$videoName")
                    putExtra("media_type", mediaType)
                    putExtra("media_id", mediaId)
                    putExtra("media_name", name)
                    putExtra("media_data", mediaData)
                    putExtra("user_id", userId)
                    putExtra("profile_id", profileId)
                    putExtra("season_id", seasonId)
                    putExtra("episode_id", episodeId)
                    putExtra("season_order", seasonOrder)
                    putExtra("episode_order", episodeOrder)
                    putExtra("season_name", seasonName)
                    putExtra("episode_name", episodeName)
                }
                context.startService(intent)
                return 1
            }
        } else {
            return 0
        }
    }

    fun pauseDownload(media_id: String, media_type: String) {
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id, media_type)
        val downloadId = downloadData["download_id"]
        if (downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.pause(downloadId.toString().toInt())
        }
    }

    fun resumeDownload(media_id: String, media_type: String) {
        /*
        Status.PAUSED: The download is currently paused.
        Status.RUNNING: The download is currently running.
        Status.QUEUED: The download is queued and waiting for its turn to start.
        Status.COMPLETE: The download has completed successfully.
        Status.CANCELED: The download has been canceled.
        Status.UNKNOWN: The status of the download is unknown or could not be determined.
        */
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id, media_type)
        val downloadId = downloadData["download_id"]
        if (downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.resume(downloadId.toString().toInt())
            ////////////
            //if video has any error....will delete video data
            val status: Status = PRDownloader.getStatus(downloadId.toString().toInt())
            if ((downloadData["status"] == DownloadManagerSTATUS.STATUS_FAILED)
                || status == Status.FAILED
                || status == Status.CANCELLED
                || status == Status.UNKNOWN
            ) {
                cancelDownload(media_id, media_type)
            }
            ///////////
        }
    }

    fun cancelDownload(media_id: String, media_type: String) {
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id, media_type)
        val downloadId = downloadData["download_id"]
        val videoPath = downloadData["video_path"]
        if (downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.cancel(downloadId.toString().toInt())
            val file = File(videoPath.toString())
            if (file.exists()) {
                file.delete()
            }
            DatabaseHelper(context).deleteMediaFromDB(media_id, media_type)
        }
    }

    fun getDownloadMediaByDownloadID(
        media_id: String,
        media_type: String
    ): ArrayList<HashMap<String, Any>> {
        return DatabaseHelper(context).getDownloadDataFromDbByDownloadId(media_id, media_type)
    }

    fun getAllDownloadMedia(user_id: String, profile_id: String): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllDownloadDataFromDB(user_id, profile_id)
    }

    fun getAllSeasons(series_id: String): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllSeasonsDownloadDataFromDB(series_id)
    }

    fun getAllEpisodes(seasons_id: String, tvshow_id: String): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllEpisodesDownloadDataFromDB(seasons_id, tvshow_id)
    }

    ////////////////////////////////////////////////////////////////

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        lactivity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        lactivity = binding.activity
    }

    override fun onDetachedFromActivity() {}
}