package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.media3.common.C.NetworkType
import java.util.*
/////
/*
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2core.Extras;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2core.MutableExtras;
import com.tonyodev.fetch2core.Reason;*/
/////
class DownloaderDowPlay(innerContext: Context) {

    //private var fetch: Fetch? = null
    private var context: Context
    init {
        context = innerContext
    }


    /*
    fun startDownload(url: String, fileName: String) {
        val fetchConfiguration: FetchConfiguration = Builder(this)
            .setDownloadConcurrentLimit(3)
            .build()
        fetch = Fetch.Impl.getInstance(fetchConfiguration)
        val url = "http:www.example.com/test.txt"
        val file = "/downloads/test.txt"
        val request = Request(url, file)
        request.setPriority(Priority.HIGH)
        request.setNetworkType(NetworkType.ALL)
        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")
        fetch.enqueue(request, { updatedRequest -> }) { error -> }
    }*/

    fun startDownloadOLD(url: String, fileName: String) {
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

    fun cancelDownload(downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId)
    }
    fun getAllDownloadMedia() {}
    fun getAllSeasons(seriesId:Int) {}
    fun getAllEpisodes(seriesId:Int, seasons:Int) {}

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


/*
// Register the BroadcastReceiver
        val filter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
            addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
            /*addAction(DownloadManager.ACTION_DOWNLOAD_CANCELLED)
            addAction(DownloadManager.ACTION_DOWNLOAD_PAUSED)
            addAction(DownloadManager.ACTION_DOWNLOAD_RESUMED)*/
        }
        val receiver = DownloadReceiver()
        context.registerReceiver(receiver, filter)
     ///////////////////////////////////////////////////////////
class DownloadReceiver : BroadcastReceiver() {
    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                // Handle download completion
                Log.d("Test Download:","A7a Download is ACTION_DOWNLOAD_COMPLETE...")

            }
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                // Handle notification click
                Log.d("Test Download:","A7a Download is ACTION_NOTIFICATION_CLICKED...")

            }
            DownloadManager.ACTION_VIEW_DOWNLOADS -> {
                // Handle view downloads action
                Log.d("Test Download:","A7a Download is ACTION_VIEW_DOWNLOADS...")

            }

            else -> {
                // Check if the download is paused or resumed
                val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query().setFilterById(downloadId!!)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_PAUSED) {
                        // Handle download pause
                        Log.d("Test Download:","A7a Download is STATUS_PAUSED...")

                    } else if (status == DownloadManager.STATUS_RUNNING) {
                        // Handle download resume
                        Log.d("Test Download:","A7a Download is STATUS_RUNNING...")
                    }
                    else if (status == DownloadManager.STATUS_FAILED) {
                        // The download has failed
                        Log.d("Test Download:","A7a Download is STATUS_FAILED...")

                    }else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // The download has completed successfully
                        Log.d("Test Download:","A7a Download is STATUS_SUCCESSFUL...")
                    }
                }
                cursor.close()
            }
        }
    }
}
*/