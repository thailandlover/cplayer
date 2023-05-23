package com.dowplay.dowplay

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.downloader.*
import com.google.gson.Gson
import com.google.gson.JsonParser

class DownloadService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    val allDownloadIdsStatus = HashMap<String, Int>()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //val notificationManager = getSystemService(NotificationManager::class.java)
        ///////////////
        Thread {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            var downloadId: Int = 0
            var currentProgressPercent: Int = 0
            var isDone: Boolean = false
            var mediaType: String? = null
            if (intent != null) {
                val url = intent.getStringExtra("url")
                val dirPath = intent.getStringExtra("dir_path")
                val fileName = intent.getStringExtra("video_name")
                val fullPath = intent.getStringExtra("full_path")
                mediaType = intent.getStringExtra("media_type")
                val mediaId = intent.getStringExtra("media_id")
                val mediaName = intent.getStringExtra("media_name")
                val mediaData = intent.getStringExtra("media_data")
                val userId = intent.getStringExtra("user_id")
                val profileId = intent.getStringExtra("profile_id")
                val seasonId = intent.getStringExtra("season_id")
                val episodeId = intent.getStringExtra("episode_id")
                val seasonOrder = intent.getStringExtra("season_order")
                val episodeOrder = intent.getStringExtra("episode_order")
                val seasonName = intent.getStringExtra("season_name")
                val episodeName = intent.getStringExtra("episode_name")
                //if has null value... need to check!!!
                val episodeJson = intent.getStringExtra("episode_json")

                Log.d("Bom Info", url.toString())
                Log.d("Bom Info", dirPath.toString())
                Log.d("Bom Info", fileName.toString())
                Log.d("Bom Info", fullPath.toString())
                Log.d("Bom Info", mediaType.toString())

                val config = PRDownloaderConfig.newBuilder()
                    .setDatabaseEnabled(true)
                    .setReadTimeout(30_000)
                    .setConnectTimeout(30_000)
                    .build()
                PRDownloader.initialize(this, config)


                downloadId = PRDownloader.download(url, dirPath, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        // Download started or resumed
                        Log.d("Bom::: ", "Download resumed")
                        val downloadData = DatabaseHelper(this).getDownloadInfoFromDB(
                            mediaId.toString(),
                            mediaType.toString()
                        )
                        /*val hasOldData =
                            DatabaseHelper(this).checkIfMediaHasDataInMainTable(mediaId.toString())*/
                        val downloadIdDB = downloadData["download_id"]
                        Log.d("WWWWWWWWL:", downloadIdDB.toString())
                        if (downloadIdDB.toString().trim().isEmpty() || downloadIdDB == null) {
                            allDownloadIdsStatus["$downloadId"] =
                                DownloadManagerSTATUS.STATUS_RUNNING
                            DatabaseHelper(this).saveDownloadDataInDB(
                                downloadId,
                                DownloadManagerSTATUS.STATUS_RUNNING,
                                0,
                                fullPath.toString(),
                                mediaName.toString(),
                                mediaType.toString(),
                                mediaId.toString(),
                                mediaData.toString(),
                                userId.toString(),
                                profileId.toString()
                            )

                            if (mediaType == "series") {
                                DatabaseHelper(this).saveSeasonDataInDB(
                                    mediaId.toString(),
                                    seasonId.toString(),
                                    seasonName.toString(),
                                    seasonOrder.toString()
                                )
                                DatabaseHelper(this).saveEpisodeDataInDB(
                                    downloadId,
                                    DownloadManagerSTATUS.STATUS_RUNNING,
                                    0,
                                    fullPath.toString(),
                                    mediaId.toString(),
                                    seasonId.toString(),
                                    episodeId.toString(),
                                    episodeName.toString(),
                                    episodeOrder.toString(),
                                    episodeJson.toString()
                                )
                            }
                            startNotification(downloadId, mediaName.toString())
                            Log.d("Bom::: ", "Is Insert ID is Insert")
                        }
                        Log.d("Bom::: ", "Download ID $downloadId")

                    }
                    .setOnPauseListener {
                        // Download paused
                        allDownloadIdsStatus["$downloadId"] = DownloadManagerSTATUS.STATUS_PAUSED
                        updateStatusDownloadInDB(
                            downloadId,
                            mediaType.toString(),
                            DownloadManagerSTATUS.STATUS_PAUSED,
                            currentProgressPercent
                        )
                        Log.d("Bom::: ", "Download paused")
                    }
                    .setOnCancelListener {
                        // Download cancelled
                        allDownloadIdsStatus["$downloadId"] = DownloadManagerSTATUS.STATUS_FAILED
                        updateStatusDownloadInDB(
                            downloadId,
                            mediaType.toString(),
                            DownloadManagerSTATUS.STATUS_FAILED,
                            currentProgressPercent
                        )
                        notificationManager.cancel(downloadId)
                        if (checkMapValuesToEndStartForegroundService(allDownloadIdsStatus)) {
                            stopService()
                        }
                        Log.d("Bom::: ", "Download cancelled")
                    }
                    .setOnProgressListener { progress ->
                        // Download progress updated
                        allDownloadIdsStatus["$downloadId"] = DownloadManagerSTATUS.STATUS_RUNNING
                        val progressPercent = progress.currentBytes * 100 / progress.totalBytes

                        if (progressPercent.toInt() != currentProgressPercent) {
                            Log.d("Bom::: ", "Download progress: ${progressPercent.toInt()}%")
                            currentProgressPercent = progressPercent.toInt()
                            updateStatusDownloadInDB(
                                downloadId,
                                mediaType.toString(),
                                DownloadManagerSTATUS.STATUS_RUNNING,
                                currentProgressPercent
                            )
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            val progress =
                                (progress.currentBytes * 100 / progress.totalBytes).toInt()
                            val notification = NotificationCompat.Builder(this, "$downloadId")
                                .setContentTitle("$mediaName")
                                .setContentText(if (mediaType == "series") ("$seasonName-$episodeName") else (""))
                                .setSmallIcon(R.drawable.play_icon)
                                .setAutoCancel(true)
                                /* .addAction(
                                     R.drawable.cancel_button_icon,
                                     "Cancel",
                                     cancelPendingIntent
                                 )*/

                                .setProgress(100, progress, false)
                                .build()
                            notificationManager.notify(downloadId, notification)

                        }
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            // Download completed
                            allDownloadIdsStatus["$downloadId"] =
                                DownloadManagerSTATUS.STATUS_SUCCESSFUL
                            Log.d("Bom::: ", "Download completed")
                            updateStatusDownloadInDB(
                                downloadId,
                                mediaType.toString(),
                                DownloadManagerSTATUS.STATUS_SUCCESSFUL,
                                100
                            )
                            notificationManager.cancel(downloadId)
                            if (checkMapValuesToEndStartForegroundService(allDownloadIdsStatus)) {
                                stopService()
                            }
                        }

                        override fun onError(error: Error?) {
                            allDownloadIdsStatus["$downloadId"] =
                                DownloadManagerSTATUS.STATUS_FAILED
                            Log.d("Bom::: ", "Download error ${error.toString()}")
                            Log.d("Bom::: ", "Download error $error")
                            Log.d(
                                "Bom::: ",
                                "Download isConnectionError ${error?.isConnectionError}"
                            )
                            Log.d("Bom::: ", "Download isServerError ${error?.isServerError}")
                            updateStatusDownloadInDB(
                                downloadId,
                                mediaType.toString(),
                                DownloadManagerSTATUS.STATUS_FAILED,
                                currentProgressPercent
                            )
                            notificationManager.cancel(downloadId)
                            if (checkMapValuesToEndStartForegroundService(allDownloadIdsStatus)) {
                                stopService()
                            }
                        }
                    })
            }
        }.start()
        return START_NOT_STICKY
    }

    private fun startNotification(downloadId: Int, title: String) {
        Log.d("Bom::: ", "Download Started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("Bom::: ", "Download ******")
            val channel = NotificationChannel(
                "$downloadId",
                title,
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, "$downloadId")
            .setContentTitle("Downloading...")
            .setSmallIcon(R.drawable.play_icon)
            .setAutoCancel(true)
            .build()
        startForeground(downloadId, notification)
    }

    private fun updateStatusDownloadInDB(
        downloadId: Int,
        mediaType: String,
        status: Int,
        currentProgressPercent: Int
    ) {
        if (mediaType == "series") {
            val status5: Status =
                PRDownloader.getStatus(downloadId)
            Log.d("WWW::: ", "$downloadId > $status5")
            DatabaseHelper(applicationContext).updateSeriesDownloadDataInDB(
                downloadId,
                status,
                currentProgressPercent,
            )
        } else {
            val status5: Status =
                PRDownloader.getStatus(downloadId)
            Log.d("WWW::: ", "$downloadId > $status5")
            DatabaseHelper(applicationContext).updateDownloadDataInDB(
                downloadId,
                status,
                currentProgressPercent,
            )
        }
    }

    fun checkMapValuesToEndStartForegroundService(map: Map<String, Int>): Boolean {
        val values = map.values
        return !values.any { it == 0 }
    }

    override fun onDestroy() {
        /*if (!isDone) {
            updateStatusDownloadInDB(
                mediaType.toString(),
                DownloadManagerSTATUS.STATUS_FAILED,
                currentProgressPercent
            )
            stopService()
        }*/
        PRDownloader.cancelAll()
        stopService()
        super.onDestroy()
    }

    private fun stopService() {
        // Stop the foreground service
        stopForeground(true)
        stopSelf()
    }
}
