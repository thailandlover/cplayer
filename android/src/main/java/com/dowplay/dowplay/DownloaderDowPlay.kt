package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.downloader.PRDownloader
import java.util.*


class DownloaderDowPlay(innerContext: Context) {

    private var context: Context
    init {
        context = innerContext
    }

    fun cancelDownload(downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId)
    }

    fun pauseDownload(downloadId: Int) {
        PRDownloader.pause(downloadId)
    }

    fun resumeDownload(downloadId: Int) {
        PRDownloader.resume(downloadId)
    }
    fun getAllDownloadMedia() {}
    fun getAllSeasons(seriesId:Int) {}
    fun getAllEpisodes(seriesId:Int, seasons:Int) {}

    fun startDownload(url: String, fileName: String) {
        //PRDownloader.initialize(context);
        // Enabling database for resume support even after the application is killed:
        var url1 = "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4"
        //var url1 = "https://thekee-m.gcdn.co/images06012022/uploads/directors/2022-10-16/z16camhTHPAt1JMh.png"
        var file1 = "${context.getExternalFilesDir(null)}"

        val intent = Intent(context, DownloadService::class.java).apply {
            putExtra("url", url1)
            putExtra("dirPath", file1)
            putExtra("fileName", "fileA7a.mp4")
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            println("A7a1")
            context.startForegroundService(intent)
        } else {
            println("A7a2")*/
            context.startService(intent)
        //}
        //context.startService(intent)
/*
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(context, config)
        val downloadId = PRDownloader.download(url1, file1, "filexxx.mp4")
            .build()
            .setOnStartOrResumeListener {

            }
    .setOnPauseListener {

    }
    .setOnCancelListener {

    }
    .setOnProgressListener { progress ->
        val progressPercent = progress.currentBytes * 100 / progress.totalBytes
        println("Download progress: $progressPercent%")
    }
    .start(object : OnDownloadListener {
        override fun onDownloadComplete() {

        }

        override fun onError(error: com.downloader.Error?) {
        }
    })*/
}

    /*
    fun startDownload(url: String, fileName: String) {
        Log.d("Test Download:", "Download is Start...")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            .setDestinationInExternalFilesDir(
                context, Environment.getExternalStorageDirectory().path + "/downloader",
                "$fileName.mp4"
            )

        val downloadId = downloadManager.enqueue(request)

        ///Save Start download info
        DatabaseHelper(context).saveDownloadDataInDB(
            downloadId,
            DownloadManagerSTATUS.STATUS_RUNNING,
            0,
            "",
            "movie",
            1,
            "",
        )

        /*val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setContentTitle(fileName)
            .setContentText("Download in progress")
            .setSmallIcon(R.drawable.play_icon)

        notificationManager.notify(downloadId.toInt(), notificationBuilder.build())*/

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Code to be executed every 5 seconds
                downloadManagerStatus(context, downloadId)
            }
        }, 0, 5000)

        Log.d("downloadId", "downloadId:::$downloadId")
        // Store the download ID for later use
        // You can use this ID to pause, resume, or cancel the download
    }
    fun pauseDownload(downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId)
        //downloadManager.pauseDownload(downloadId);
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("Range")
    fun resumeDownload(downloadId: Long) {
        //var dataDB =  DatabaseHelper(context).getDownloadDataFromDB(downloadId)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //////////
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        /////////

        //setRequiresCharging(false)
        //setRequiresDeviceIdle(false)
        val cursor = downloadManager.query(query)
        Log.d("cursor.moveToFirst","${cursor.moveToFirst()}")

        if (cursor.moveToFirst()) {
            Log.d("cursor.columnCount","${cursor.columnCount}")
            Log.d("cursor.columnNames","${cursor.columnNames.first()}")
            Log.d("cursor.COLUMN_URI","${cursor.getColumnIndex(DownloadManager.COLUMN_URI)}")
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val uri = Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI)))
            Log.d("uri_COLUMN_URI", "$uri")
            Log.d("status_COLUMN_STATUS", "$status")
            val request = DownloadManager.Request(uri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("The Simpsons in Plusaversary")
                 .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                    .setDestinationInExternalFilesDir(
                        context, Environment.getExternalStorageDirectory().path + "/downloader",
                        "The Simpsons in Plusaversary.mp4")
            }
            downloadManager.enqueue(request)
        }
        cursor.close()
    }


    @SuppressLint("Range")
    fun downloadManagerStatus(context: Context, downloadId: Long): Int {
        var dataDB =  DatabaseHelper(context).getDownloadDataFromDB(downloadId)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    // The download has completed successfully
                    Log.d("Test Download:", "A7a Download is STATUS_SUCCESSFUL...")
                    DatabaseHelper(context).updateDownloadDataInDB(
                        downloadId,
                        DownloadManagerSTATUS.STATUS_SUCCESSFUL,
                        100,
                        "",
                        "movie",
                        1,
                        "",
                    )
                    return DownloadManagerSTATUS.STATUS_SUCCESSFUL
                }
                DownloadManager.STATUS_FAILED -> {
                    // The download has failed
                    Log.d("Test Download:", "A7a Download is STATUS_FAILED...")
                    DatabaseHelper(context).updateDownloadDataInDB(
                        downloadId,
                        DownloadManagerSTATUS.STATUS_FAILED,
                        0,
                        "",
                        "movie",
                        1,
                        "",
                    )
                    return DownloadManagerSTATUS.STATUS_FAILED
                }
                DownloadManager.STATUS_RUNNING -> {
                    // The download is still in progress
                    Log.d("Test Download:", "A7a Download is STATUS_RUNNING...")
                    val totalBytes =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val downloadedBytes =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val progress = (downloadedBytes * 100 / totalBytes).toInt()
                    // Update your UI with the progress value
                    DatabaseHelper(context).updateDownloadDataInDB(
                        downloadId,
                        DownloadManagerSTATUS.STATUS_RUNNING,
                        progress,
                        "",
                        "movie",
                        1,
                        "",
                    )
                    return DownloadManagerSTATUS.STATUS_RUNNING
                }
                DownloadManager.STATUS_PAUSED -> {
                    // The download has been paused
                    Log.d("Test Download:", "A7a Download is STATUS_PAUSED...")
                    DatabaseHelper(context).updateDownloadDataInDB(
                        downloadId,
                        DownloadManagerSTATUS.STATUS_PAUSED,
                        dataDB["progress"] as Int,
                        "",
                        "movie",
                        1,
                        "",
                    )
                    return DownloadManagerSTATUS.STATUS_PAUSED
                }
            }
        }
        cursor.close()
        return DownloadManagerSTATUS.STATUS_UNKNOWN
    }
     */
}

class DownloadManagerSTATUS{
    //println("${STATUS.ordinal} = ${STATUS.name}")
    companion object {
        const val STATUS_RUNNING = 0
        const val STATUS_FAILED = 1
        const val STATUS_SUCCESSFUL = 3
        const val STATUS_PAUSED = 4
        const val STATUS_UNKNOWN = 5
    }
}