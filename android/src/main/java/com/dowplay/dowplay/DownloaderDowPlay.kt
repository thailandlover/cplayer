package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Objects
import java.util.Timer
import java.util.TimerTask

class DownloaderDowPlay(innerContext: Context) {
    /*private fun startDownload(context: Context, url: String, fileName: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadId = downloadManager.enqueue(request)
        // Store the download ID for later use
        // You can use this ID to pause, resume, or cancel the download
    }*/
    private var context: Context
    init {
        context = innerContext
    }


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
        saveDownloadDataInDB(downloadId,
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

    fun saveDownloadDataInDB(
        download_id: Long,
        status: Int,
        progress: Int,
        name: String,
        media_type: String,
        media_id: Int,
        media_data: String
    ): Long {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COL_download_id, download_id)
            put(DatabaseHelper.COL_status, status)
            put(DatabaseHelper.COL_progress, progress)
            put(DatabaseHelper.COL_name, name)
            put(DatabaseHelper.COL_media_type, media_type)
            put(DatabaseHelper.COL_media_id, media_id)
            put(DatabaseHelper.COL_media_data, media_data)
        }
        val insertCount = db.insert(DatabaseHelper.my_table, null, values)
        db.close()
        dbHelper.close()
        return insertCount
    }
    ///////////////////////////////////////////////
    fun updateDownloadDataInDB(
        download_id: Long,
        status: Int,
        progress: Int,
        name: String,
        media_type: String,
        media_id: Int,
        media_data: String
    ): Int {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COL_download_id, download_id)
            put(DatabaseHelper.COL_status, status)
            put(DatabaseHelper.COL_progress, progress)
            put(DatabaseHelper.COL_name, name)
            put(DatabaseHelper.COL_media_type, media_type)
            put(DatabaseHelper.COL_media_id, media_id)
            put(DatabaseHelper.COL_media_data, media_data)
        }

        val selection = "download_id = ?"
        val selectionArgs = arrayOf("$download_id")

        val updateCount = db.update(
            DatabaseHelper.my_table,
            values,
            selection,
            selectionArgs
        )
        db.close()
        dbHelper.close()
        return updateCount
    }
    @SuppressLint("Range")
    fun getDownloadDataFromDB(download_id: Long): HashMap<String,Any>{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${DatabaseHelper.COL_download_id}, " +
                "${DatabaseHelper.COL_status}, ${DatabaseHelper.COL_progress}," +
                "${DatabaseHelper.COL_name},${DatabaseHelper.COL_media_type}," +
                "${DatabaseHelper.COL_media_id},${DatabaseHelper.COL_media_data} FROM ${DatabaseHelper.my_table} WHERE download_id = ?"
        val selectionArgs = arrayOf(""+download_id)

        val cursor = db.rawQuery(query, selectionArgs)

        val mapData = HashMap<String,Any>()
        if (cursor.moveToFirst()) {
            val downloadId = cursor.getLong(cursor.getColumnIndex( DatabaseHelper.COL_download_id))
            val status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_status))
            val progress = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_progress))
            val name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_name))
            val mediaType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_media_type))
            val mediaId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_media_id))
            val mediaData = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_media_data))
            mapData["download_id"] = downloadId
            mapData["status"] = status
            mapData["progress"] = progress
            mapData["name"] = name
            mapData["media_type"] = mediaType
            mapData["media_id"] = mediaId
            mapData["media_data"] = mediaData
            // do something with retrieved data
        }

        Log.d("Sqlite Data:","${mapData.toString()}")

        cursor.close()
        db.close()
        return mapData
    }
    @SuppressLint("Range")
    fun downloadManagerStatus(context: Context, downloadId: Long): Int {
        getDownloadDataFromDB(downloadId)

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
                    updateDownloadDataInDB(downloadId,
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
                    updateDownloadDataInDB(downloadId,
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