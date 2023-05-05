package com.dowplay.dowplay

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import java.util.*
import kotlin.math.abs

class DownloadService : Service() {
    private var downloadId: Int = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    var currentProgressPercent: Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        ///////////////
        if (intent != null) {
            val url = intent.getStringExtra("url")
            val dirPath = intent.getStringExtra("dir_path")
            val fileName = intent.getStringExtra("video_name")
            val fullPath = intent.getStringExtra("full_path")
            val mediaType = intent.getStringExtra("media_type")
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

            Log.d("Bom Info", url.toString())
            Log.d("Bom Info", dirPath.toString())
            Log.d("Bom Info", fileName.toString())
            Log.d("Bom Info", fullPath.toString())
            Log.d("Bom Info", mediaType.toString())

            val config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                //.setReadTimeout(30000)
                //.setConnectTimeout(30000)
                .build()
            PRDownloader.initialize(this, config)
            downloadId = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener {
                    // Download started or resumed
                    Log.d("Bom::: ", "Download resumed")
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
                            episodeOrder.toString()
                        )
                    }
                    startNotification(abs(downloadId), mediaName.toString())
                    Log.d("Bom::: ", "Download ID $downloadId")
                    //Log.d("Bom::: ", "Is Insert ID $isInsert")
                }
                .setOnPauseListener {
                    // Download paused
                    updateStatusDownloadInDB(
                        mediaType.toString(),
                        DownloadManagerSTATUS.STATUS_PAUSED,
                        currentProgressPercent
                    )
                    Log.d("Bom::: ", "Download paused")
                }
                .setOnCancelListener {
                    // Download cancelled
                    updateStatusDownloadInDB(
                        mediaType.toString(),
                        DownloadManagerSTATUS.STATUS_FAILED,
                        currentProgressPercent
                    )
                    Log.d("Bom::: ", "Download cancelled")
                }
                .setOnProgressListener { progress ->
                    // Download progress updated

                    val progressPercent = progress.currentBytes * 100 / progress.totalBytes

                    if (progressPercent.toInt() != currentProgressPercent) {
                        Log.d("Bom::: ", "Download progress: ${progressPercent.toInt()}%")
                        currentProgressPercent = progressPercent.toInt()
                        updateStatusDownloadInDB(
                            mediaType.toString(),
                            DownloadManagerSTATUS.STATUS_RUNNING,
                            currentProgressPercent
                        )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val notificationManager =
                            getSystemService(NotificationManager::class.java)
                        val progress =
                            (progress.currentBytes * 100 / progress.totalBytes).toInt()
                        val notification = NotificationCompat.Builder(this, "${abs(downloadId)}")
                            .setContentTitle("$mediaName")
                            .setContentText("$seasonName-$episodeName")
                            .setSmallIcon(R.drawable.play_icon)
                            .setProgress(100, progress, false)
                            .build()
                        notificationManager.notify(abs(downloadId), notification)
                    }
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        // Download completed
                        Log.d("Bom::: ", "Download completed")
                        updateStatusDownloadInDB(
                            mediaType.toString(),
                            DownloadManagerSTATUS.STATUS_SUCCESSFUL,
                            100
                        )
                        stopSelf()
                    }

                    override fun onError(error: com.downloader.Error?) {
                        Log.d("Bom::: ", "Download error $error")
                        updateStatusDownloadInDB(
                            mediaType.toString(),
                            DownloadManagerSTATUS.STATUS_FAILED,
                            currentProgressPercent
                        )
                        stopSelf()
                    }
                })
        }
        return START_NOT_STICKY
    }

    private fun startNotification(downloadId: Int, title: String) {
        Log.d("Bom::: ", "Download Started")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
            .build()
        startForeground(downloadId, notification)
    }

    private fun updateStatusDownloadInDB(
        mediaType: String,
        status: Int,
        currentProgressPercent: Int
    ) {
        if (mediaType == "series") {
            DatabaseHelper(applicationContext).updateSeriesDownloadDataInDB(
                downloadId,
                status,
                currentProgressPercent,
            )
        } else {
            DatabaseHelper(applicationContext).updateDownloadDataInDB(
                downloadId,
                DownloadManagerSTATUS.STATUS_FAILED,
                currentProgressPercent,
            )
        }
    }

    override fun onDestroy() {
        PRDownloader.cancel(downloadId)
        super.onDestroy()
    }
}