package com.dowplay.dowplay

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.app.NotificationCompat

class Downloader {
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

    private fun startDownload(context: Context, url: String, fileName: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadId = downloadManager.enqueue(request)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setContentTitle(fileName)
            .setContentText("Download in progress")
            .setSmallIcon(R.drawable.play_icon)

        notificationManager.notify(downloadId.toInt(), notificationBuilder.build())

        // Store the download ID for later use
        // You can use this ID to pause, resume, or cancel the download
    }
}