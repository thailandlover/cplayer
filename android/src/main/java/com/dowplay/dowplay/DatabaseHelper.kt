package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(innerContext: Context) : SQLiteOpenHelper(innerContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private var context: Context

    init {
        context = innerContext
    }

    companion object {
        private const val DATABASE_NAME = "downloader_db"
        private const val DATABASE_VERSION = 1
        //
        const val my_table = "downloader_table"
        //
        const val COL_download_id: String = "download_id"
        const val COL_status: String = "status"
        const val COL_progress: String = "progress"
        const val COL_video_path: String = "video_path"
        const val COL_name: String = "name"
        const val COL_media_type: String = "media_type"
        const val COL_media_id: String = "media_id"
        const val COL_media_data: String = "media_data"
        const val COL_user_id: String = "user_id"
        const val COL_profile_id: String = "profile_id"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $my_table ($COL_download_id NUMBER PRIMARY KEY, $COL_status NUMBER,  $COL_progress NUMBER,$COL_video_path TEXT, $COL_name TEXT, $COL_media_type TEXT, $COL_media_id TEXT, $COL_media_data TEXT, $COL_user_id TEXT, $COL_profile_id TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $my_table")
        onCreate(db)
    }

    fun saveDownloadDataInDB(
        download_id: Int,
        status: Int,
        progress: Int,
        video_path: String,
        name: String,
        media_type: String,
        media_id: String,
        media_data: String,
        user_id: String,
        profile_id: String,

    ): Long {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_download_id, download_id)
            put(COL_status, status)
            put(COL_progress, progress)
            put(COL_video_path, video_path)
            put(COL_name, name)
            put(COL_media_type, media_type)
            put(COL_media_id, media_id)
            put(COL_media_data, media_data)
            put(COL_user_id, user_id)
            put(COL_profile_id, profile_id)
        }
        val insertCount = db.insert(my_table, null, values)
        db.close()
        dbHelper.close()
        return insertCount
    }
    ///////////////////////////////////////////////
    fun updateDownloadDataInDB(
        download_id: Int,
        status: Int,
        progress: Int,
        //name: String,
        //media_type: String,
        //media_id: Int,
        //media_data: String
    ): Int {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_download_id, download_id)
            put(COL_status, status)
            put(COL_progress, progress)
            //put(COL_name, name)
            //put(COL_media_type, media_type)
            //put(COL_media_id, media_id)
            //put(COL_media_data, media_data)
        }
        print("A7a::: $progress - $values")
        val selection = "download_id = ?"
        val selectionArgs = arrayOf("$download_id")

        val updateCount = db.update(
            my_table,
            values,
            selection,
            selectionArgs
        )
        db.close()
        dbHelper.close()
        return updateCount
    }
    @SuppressLint("Range")
    fun getDownloadDataFromDbByDownloadId(download_id: Int): HashMap<String,Any>{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_download_id}, " +
                "${COL_status}, ${COL_progress}, ${COL_video_path}," +
                "${COL_name},${COL_media_type}," +
                "${COL_media_id},${COL_media_data}," +
                "${COL_user_id},${COL_profile_id} FROM ${my_table} WHERE download_id = ?"
        val selectionArgs = arrayOf(""+download_id)

        val cursor = db.rawQuery(query, selectionArgs)

        val mapData = HashMap<String,Any>()
        if (cursor.moveToFirst()) {
            val downloadId = cursor.getInt(cursor.getColumnIndex(COL_download_id))
            val status = cursor.getInt(cursor.getColumnIndex(COL_status))
            val progress = cursor.getInt(cursor.getColumnIndex(COL_progress))
            val videoPath = cursor.getString(cursor.getColumnIndex(COL_video_path))
            val name = cursor.getString(cursor.getColumnIndex(COL_name))
            val mediaType = cursor.getString(cursor.getColumnIndex(COL_media_type))
            val mediaId = cursor.getString(cursor.getColumnIndex(COL_media_id))
            val mediaData = cursor.getString(cursor.getColumnIndex(COL_media_data))
            val userId = cursor.getString(cursor.getColumnIndex(COL_user_id))
            val profileId = cursor.getString(cursor.getColumnIndex(COL_profile_id))
            mapData["download_id"] = downloadId
            mapData["status"] = status
            mapData["progress"] = progress
            mapData["video_path"] = videoPath
            mapData["name"] = name
            mapData["media_type"] = mediaType
            mapData["media_id"] = mediaId
            mapData["media_data"] = mediaData
            mapData["user_id"] = userId
            mapData["profile_id"] = profileId
        }

        Log.d("Sqlite Data:","${mapData.toString()}")

        cursor.close()
        db.close()
        return mapData
    }
    @SuppressLint("Range")
    fun getAllDownloadDataFromDB(): HashMap<String,Any>{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_download_id}, " +
                "${COL_status}, ${COL_progress}, ${COL_video_path}," +
                "${COL_name},${COL_media_type}," +
                "${COL_media_id},${COL_media_data}, " +
                "${COL_user_id},${COL_profile_id} FROM $my_table"

        val cursor = db.rawQuery(query,null)

        val mapData = HashMap<String,Any>()
        if (cursor.moveToFirst()) {
            val downloadId = cursor.getInt(cursor.getColumnIndex(COL_download_id))
            val status = cursor.getInt(cursor.getColumnIndex(COL_status))
            val progress = cursor.getInt(cursor.getColumnIndex(COL_progress))
            val videoPath = cursor.getString(cursor.getColumnIndex(COL_video_path))
            val name = cursor.getString(cursor.getColumnIndex(COL_name))
            val mediaType = cursor.getString(cursor.getColumnIndex(COL_media_type))
            val mediaId = cursor.getString(cursor.getColumnIndex(COL_media_id))
            val mediaData = cursor.getString(cursor.getColumnIndex(COL_media_data))
            val userId = cursor.getString(cursor.getColumnIndex(COL_user_id))
            val profileId = cursor.getString(cursor.getColumnIndex(COL_profile_id))
            mapData["download_id"] = downloadId
            mapData["status"] = status
            mapData["progress"] = progress
            mapData["video_path"] = videoPath
            mapData["name"] = name
            mapData["media_type"] = mediaType
            mapData["media_id"] = mediaId
            mapData["media_data"] = mediaData
            mapData["user_id"] = userId
            mapData["profile_id"] = profileId
        }

        Log.d("Sqlite Data:","$mapData")

        cursor.close()
        db.close()
        return mapData
    }
}