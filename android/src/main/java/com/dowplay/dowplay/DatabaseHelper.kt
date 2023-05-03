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
        const val COL_name: String = "name"
        const val COL_media_type: String = "media_type"
        const val COL_media_id: String = "media_id"
        const val COL_media_data: String = "media_data"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $my_table (id INTEGER PRIMARY KEY AUTOINCREMENT, $COL_download_id LONG, $COL_status NUMBER,  $COL_progress NUMBER, $COL_name TEXT, $COL_media_type TEXT, $COL_media_id NUMBER, $COL_media_data TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $my_table")
        onCreate(db)
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
}