package com.dowplay.dowplay

import android.content.Context
import android.content.Intent
import android.util.Log
import com.downloader.PRDownloader
import java.io.File
import java.security.SecureRandom
import java.math.BigInteger


class DownloaderDowPlay(innerContext: Context) {

    private var context: Context
    init {
        context = innerContext
    }
    fun generateRandomToken(length: Int): String {
        val secureRandom = SecureRandom()
        val token = BigInteger(130, secureRandom).toString(32)
        return token.take(length)
    }

    fun startDownload(url: String,name:String, mediaType: String, mediaId:String, mediaData: String,userId:String, profileId:String
    ,seasonId:String, episodeId:String, seasonOrder:String,episodeOrder:String, seasonName:String,episodeName:String): Int {
        //PRDownloader.initialize(context);
        // Enabling database for resume support even after the application is killed:
        //val url1 = "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4"
        //var url1 = "https://thekee.gcdn.co/video/m-159n/English/Drama/Deathwatch.2002.1080p.V1.mp4"
        //for save in public pkg app
        //var dirPath = "${context.getExternalFilesDir(null)}"
        //for save in private pkg app
        val downloadInfo = DatabaseHelper(context).getDownloadInfoFromDB(mediaId, mediaType)
        Log.d("startDownload Method","this video is downloaded..."+mediaId+" > "+mediaType+" > "+downloadInfo["status"])
        if(downloadInfo["status"] == DownloadManagerSTATUS.STATUS_SUCCESSFUL || downloadInfo["status"] == DownloadManagerSTATUS.STATUS_RUNNING){
            Log.d("startDownload Method","this video is downloaded...")
            return 0
        }else {
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
    }
    fun pauseDownload(media_id: String,media_type: String) {
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id,media_type)
        val downloadId = downloadData["download_id"]
        if(downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.pause(downloadId.toString().toInt())
        }
    }

    fun resumeDownload(media_id: String,media_type: String) {
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id,media_type)
        val downloadId = downloadData["download_id"]
        if(downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.resume(downloadId.toString().toInt())
        }
    }
    fun cancelDownload(media_id: String,media_type: String) {
        val downloadData = DatabaseHelper(context).getDownloadInfoFromDB(media_id,media_type)
        val downloadId = downloadData["download_id"]
        val videoPath = downloadData["video_path"]
        if(downloadId.toString().trim().isNotEmpty() && downloadId != null) {
            PRDownloader.cancel(downloadId.toString().toInt())
            val file = File(videoPath.toString())
            if (file.exists()) {
                file.delete()
            }
            DatabaseHelper(context).deleteMediaFromDB(media_id, media_type)
        }
    }
    fun getDownloadMediaByDownloadID(media_id: String, media_type:String): ArrayList<HashMap<String, Any>> {
        return DatabaseHelper(context).getDownloadDataFromDbByDownloadId(media_id,media_type)
    }
    fun getAllDownloadMedia(): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllDownloadDataFromDB()
    }
    fun getAllSeasons(series_id:String): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllSeasonsDownloadDataFromDB(series_id)
    }
    fun getAllEpisodes(seasons_id:String, tvshow_id:String): List<HashMap<String, Any>> {
        return DatabaseHelper(context).getAllEpisodesDownloadDataFromDB(seasons_id,tvshow_id)
    }


  /*
    fun startDownload(url: String, fileName: String) {
        //PRDownloader.initialize(context);
        // Enabling database for resume support even after the application is killed:
        var url1 = "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4"
        //var url1 = "https://thekee.gcdn.co/video/m-159n/English/Drama/Deathwatch.2002.1080p.V1.mp4"
        //var url1 = "https://thekee-m.gcdn.co/images06012022/uploads/directors/2022-10-16/z16camhTHPAt1JMh.png"
        var file1 = "${context.getExternalFilesDir(null)}"

        val intent = Intent(context, DownloadService::class.java).apply {
            putExtra("url", url1)
            putExtra("dirPath", file1)
            putExtra("fileName", "fileBom.mp4")
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            println("Bom1")
            context.startForegroundService(intent)
        } else {
            println("Bom2")*/
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
*/
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
                    Log.d("Test Download:", "Bom Download is STATUS_SUCCESSFUL...")
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
                    Log.d("Test Download:", "Bom Download is STATUS_FAILED...")
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
                    Log.d("Test Download:", "Bom Download is STATUS_RUNNING...")
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
                    Log.d("Test Download:", "Bom Download is STATUS_PAUSED...")
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