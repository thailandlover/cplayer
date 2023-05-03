package com.dowplay.dowplay

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


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
}