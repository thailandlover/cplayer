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
    var isrun : Boolean = true

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
                val dirPath = intent.getStringExtra("dirPath")
                val fileName = intent.getStringExtra("fileName")
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
                        Log.d("A7a::: ", "Download ID $downloadId")

                    }
                    .setOnPauseListener {
                        // Download paused
                        Log.d("A7a::: ", "Download paused")
                    }
                    .setOnCancelListener {
                        // Download cancelled
                        Log.d("A7a::: ", "Download cancelled")
                    }
                    .setOnProgressListener { progress ->
                        // Download progress updated
                        val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                        Log.d("A7a::: ", "Download progress: $progressPercent%")
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
                            stopSelf()
                        }

                        override fun onError(error: com.downloader.Error?) {
                            Log.d("A7a::: ", "Download error")
                        }
                    })

                /*val timer = Timer()
                timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        // Code to be executed every 5 seconds
                        if(isrun) {
                            PRDownloader.pause(downloadId)
                            isrun = false
                        }else{
                            PRDownloader.resume(downloadId)
                            isrun = true
                        }
                    }
                }, 0, 10000)*/

            }
            return START_NOT_STICKY
        }

        override fun onDestroy() {
            PRDownloader.cancel(downloadId)
            super.onDestroy()
        }
    }