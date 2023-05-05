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

class DownloadService : Service() {
        private var downloadId: Int = 0

        override fun onBind(intent: Intent): IBinder? {
            return null
        }

    var currentProgressPercent:Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("A7a::: ", "Download Started")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "1",
                    "Downloads",
                    NotificationManager.IMPORTANCE_LOW
                )
                getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(this, "1")
                .setContentTitle("Downloading...")
                .setSmallIcon(R.drawable.play_icon)
                .build()

            startForeground(1, notification)
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
                Log.d("A7a Info", url.toString())
                Log.d("A7a Info", dirPath.toString())
                Log.d("A7a Info", fileName.toString())
                Log.d("A7a Info", fullPath.toString())

                val config = PRDownloaderConfig.newBuilder()
                    .setDatabaseEnabled(true)
                    .setReadTimeout(30000)
                    .setConnectTimeout(30000)
                    .build()
                PRDownloader.initialize(this, config)
                downloadId = PRDownloader.download(url, dirPath, fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        // Download started or resumed
                        Log.d("A7a::: ", "Download resumed")
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
                        Log.d("A7a::: ", "Download ID $downloadId")
                        //Log.d("A7a::: ", "Is Insert ID $isInsert")
                    }
                    .setOnPauseListener {
                        // Download paused
                        DatabaseHelper(this).updateDownloadDataInDB(
                            downloadId,
                            DownloadManagerSTATUS.STATUS_PAUSED,
                            currentProgressPercent,
                        )
                        Log.d("A7a::: ", "Download paused")
                    }
                    .setOnCancelListener {
                        // Download cancelled
                        DatabaseHelper(this).updateDownloadDataInDB(
                            downloadId,
                            DownloadManagerSTATUS.STATUS_FAILED,
                            currentProgressPercent,
                        )
                        Log.d("A7a::: ", "Download cancelled")
                    }
                    .setOnProgressListener { progress ->
                        // Download progress updated

                        val progressPercent = progress.currentBytes * 100 / progress.totalBytes

                        if(progressPercent.toInt() != currentProgressPercent) {
                            Log.d("A7a::: ", "Download progress: ${progressPercent.toInt()}%")
                            currentProgressPercent = progressPercent.toInt()
                            DatabaseHelper(this).updateDownloadDataInDB(
                                downloadId,
                                DownloadManagerSTATUS.STATUS_RUNNING,
                                currentProgressPercent,
                            )
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val notificationManager =
                                getSystemService(NotificationManager::class.java)
                            val progress =
                                (progress.currentBytes * 100 / progress.totalBytes).toInt()
                            val notification = NotificationCompat.Builder(this, "1")
                                .setContentTitle("Downloading...")
                                .setSmallIcon(R.drawable.play_icon)
                                .setProgress(100, progress, false)
                                .build()
                            notificationManager.notify(1, notification)
                        }
                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            // Download completed
                            Log.d("A7a::: ", "Download completed")
                            DatabaseHelper(applicationContext).updateDownloadDataInDB(
                                downloadId,
                                DownloadManagerSTATUS.STATUS_SUCCESSFUL,
                                100,
                            )
                            stopSelf()
                        }

                        override fun onError(error: com.downloader.Error?) {
                            Log.d("A7a::: ", "Download error $error")
                            DatabaseHelper(applicationContext).updateDownloadDataInDB(
                                downloadId,
                                DownloadManagerSTATUS.STATUS_FAILED,
                                currentProgressPercent,
                            )
                            stopSelf()
                        }
                    })
            }
            return START_NOT_STICKY
        }

        override fun onDestroy() {
            PRDownloader.cancel(downloadId)
            super.onDestroy()
        }
    }