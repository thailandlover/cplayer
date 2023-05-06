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
        const val main_table = "downloader_table"
        const val seasons_table = "seasons_table"
        const val episodes_table = "episodes_table"

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

        /////////////////////////////////
        //Col for seasons
        //id, tv_show_id, season_id, name, order
        //Col for episodes
        //id, tv_show_id, season_id, episode_id, name, order
        //const val COL_tv_show_id: String = "tv_show_id"
        const val COL_season_id: String = "season_id"
        const val COL_episode_id: String = "episode_id"
        const val COL_order: String = "col_order"

    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $main_table ($COL_download_id NUMBER PRIMARY KEY ON CONFLICT REPLACE, $COL_status NUMBER,  $COL_progress NUMBER,$COL_video_path TEXT, $COL_name TEXT, $COL_media_type TEXT, $COL_media_id TEXT UNIQUE ON CONFLICT REPLACE, $COL_media_data TEXT, $COL_user_id TEXT, $COL_profile_id TEXT )")
        db.execSQL("CREATE TABLE $seasons_table (id INTEGER PRIMARY KEY AUTOINCREMENT, $COL_media_id TEXT,  $COL_season_id TEXT , $COL_name TEXT, $COL_order TEXT, UNIQUE($COL_media_id, $COL_season_id) ON CONFLICT REPLACE)")
        db.execSQL("CREATE TABLE $episodes_table ($COL_download_id NUMBER PRIMARY KEY, $COL_status NUMBER,  $COL_progress NUMBER,$COL_video_path TEXT, $COL_media_id TEXT,  $COL_season_id TEXT,$COL_episode_id TEXT , $COL_name TEXT, $COL_order TEXT, UNIQUE($COL_media_id, $COL_season_id, $COL_episode_id) ON CONFLICT REPLACE)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $main_table")
        db.execSQL("DROP TABLE IF EXISTS $seasons_table")
        db.execSQL("DROP TABLE IF EXISTS $episodes_table")
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
        val insertCount = db.insert(main_table, null, values)
        db.close()
        dbHelper.close()
        return insertCount
    }

    fun saveSeasonDataInDB(
        media_id: String,
        season_id: String,
        name: String,
        order: String,
    ): Long {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_media_id, media_id)
            put(COL_season_id, season_id)
            put(COL_name, name)
            put(COL_order, order)
        }
        val insertCount = db.insert(seasons_table, null, values)
        db.close()
        dbHelper.close()
        return insertCount
    }

    fun saveEpisodeDataInDB(
        download_id: Int,
        status: Int,
        progress: Int,
        video_path: String,
        media_id: String,
        season_id: String,
        episode_id: String,
        name: String,
        order: String,
    ): Long {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_download_id, download_id)
            put(COL_status, status)
            put(COL_progress, progress)
            put(COL_video_path, video_path)
            put(COL_media_id, media_id)
            put(COL_season_id, season_id)
            put(COL_episode_id, episode_id)
            put(COL_name, name)
            put(COL_order, order)
        }
        val insertCount = db.insert(episodes_table, null, values)
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
        print("Bom::: $progress - $values")
        val selection = "download_id = ?"
        val selectionArgs = arrayOf("$download_id")

        val updateCount = db.update(
            main_table,
            values,
            selection,
            selectionArgs
        )
        db.close()
        dbHelper.close()
        return updateCount
    }

    ///////////////////////////////////////////////
    fun updateSeriesDownloadDataInDB(
        download_id: Int,
        status: Int,
        progress: Int,
    ): Int {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_download_id, download_id)
            put(COL_status, status)
            put(COL_progress, progress)
        }
        print("Bom::: $progress - $values")
        val selection = "download_id = ?"
        val selectionArgs = arrayOf("$download_id")

        val updateCount = db.update(
            episodes_table,
            values,
            selection,
            selectionArgs
        )
        db.close()
        dbHelper.close()
        return updateCount
    }

    @SuppressLint("Range")
    fun getDownloadDataFromDbByDownloadId(
        media_id: String,
        media_type: String
    ): ArrayList<HashMap<String, Any>> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_download_id}, " +
                "${COL_status}, ${COL_progress}, ${COL_video_path}," +
                "${COL_name},${COL_media_type}," +
                "${COL_media_id},${COL_media_data}," +
                "${COL_user_id},${COL_profile_id} FROM $main_table WHERE $COL_media_id = ? AND $COL_media_type = ?"
        val selectionArgs = arrayOf(media_id, media_type)

        val cursor = db.rawQuery(query, selectionArgs)

        val allDownloadData = ArrayList<HashMap<String, Any>>()
        val mapData = HashMap<String, Any>()
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
            allDownloadData += mapData
        }

        Log.d("Sqlite Data:", "$mapData")

        cursor.close()
        db.close()
        return allDownloadData
    }

    @SuppressLint("Range")
    fun getAllDownloadDataFromDB(): List<HashMap<String, Any>> {

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_download_id}, " +
                "${COL_status}, ${COL_progress}, ${COL_video_path}," +
                "${COL_name},${COL_media_type}," +
                "${COL_media_id},${COL_media_data}, " +
                "${COL_user_id},${COL_profile_id} FROM $main_table"

        val cursor = db.rawQuery(query, null)

        val allDownloadData = ArrayList<HashMap<String, Any>>()
        val mapData = HashMap<String, Any>()
        while (cursor.moveToNext()) {
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
            allDownloadData += mapData
        }

        Log.d("Sqlite Data:", "$allDownloadData")

        cursor.close()
        db.close()
        return allDownloadData
    }

    @SuppressLint("Range")
    fun getAllSeasonsDownloadDataFromDB(series_id: String): List<HashMap<String, Any>> {

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_media_id},${COL_season_id}," +
                "${COL_name},${COL_order} FROM $seasons_table WHERE $COL_media_id = ? Order by $COL_order ASC"

        val selectionArgs = arrayOf(series_id)

        val cursor = db.rawQuery(query, selectionArgs)

        val allDownloadData = ArrayList<HashMap<String, Any>>()
        val mapData = HashMap<String, Any>()
        while (cursor.moveToNext()) {
            val mediaId = cursor.getString(cursor.getColumnIndex(COL_media_id))
            val seasonId = cursor.getString(cursor.getColumnIndex(COL_season_id))
            val name = cursor.getString(cursor.getColumnIndex(COL_name))
            val order = cursor.getString(cursor.getColumnIndex(COL_order))

            mapData["media_id"] = mediaId
            mapData["season_id"] = seasonId
            mapData["name"] = name
            mapData["order"] = order

            allDownloadData += mapData
        }

        Log.d("Sqlite Data:", "$allDownloadData")

        cursor.close()
        db.close()
        return allDownloadData
    }

    @SuppressLint("Range")
    fun getAllEpisodesDownloadDataFromDB(
        season_id: String,
        tvshow_id: String
    ): List<HashMap<String, Any>> {

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT ${COL_download_id},${COL_status}," +
                "${COL_progress},${COL_video_path}," +
                "${COL_media_id}, ${COL_season_id},${COL_episode_id}," +
                "${COL_name},${COL_order} FROM $episodes_table WHERE $COL_season_id = ? AND $COL_media_id = ? Order by $COL_order ASC"

        val selectionArgs = arrayOf(season_id, tvshow_id)

        val cursor = db.rawQuery(query, selectionArgs)

        val allDownloadData = ArrayList<HashMap<String, Any>>()
        val mapData = HashMap<String, Any>()
        while (cursor.moveToNext()) {
            val downloadId = cursor.getInt(cursor.getColumnIndex(COL_download_id))
            val status = cursor.getInt(cursor.getColumnIndex(COL_status))
            val progress = cursor.getInt(cursor.getColumnIndex(COL_progress))
            val videoPath = cursor.getString(cursor.getColumnIndex(COL_video_path))
            val mediaId = cursor.getString(cursor.getColumnIndex(COL_media_id))
            val seasonId = cursor.getString(cursor.getColumnIndex(COL_season_id))
            val episodeId = cursor.getString(cursor.getColumnIndex(COL_episode_id))
            val name = cursor.getString(cursor.getColumnIndex(COL_name))
            val order = cursor.getString(cursor.getColumnIndex(COL_order))

            mapData["download_id"] = downloadId
            mapData["status"] = status
            mapData["progress"] = progress
            mapData["video_path"] = videoPath
            mapData["media_id"] = mediaId
            mapData["season_id"] = seasonId
            mapData["episode_id"] = episodeId
            mapData["name"] = name
            mapData["order"] = order

            allDownloadData += mapData
        }

        Log.d("Sqlite Data:", "$allDownloadData")

        cursor.close()
        db.close()
        return allDownloadData
    }

    @SuppressLint("Range")
    fun getDownloadInfoFromDB(
        media_id: String,
        media_type: String
    ): HashMap<String, Any> {

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        var query = if (media_type == "series") {
            "SELECT $COL_download_id, $COL_video_path, $COL_status FROM $episodes_table WHERE $COL_episode_id = ?"
        } else {
            "SELECT $COL_download_id, $COL_video_path, $COL_status FROM $main_table WHERE $COL_media_id = ?"
        }

        val selectionArgs = arrayOf(media_id)
        val cursor = db.rawQuery(query, selectionArgs)

        val downloadData = HashMap<String, Any>()
        if (cursor.moveToFirst()) {
            val downloadId = cursor.getInt(cursor.getColumnIndex(COL_download_id))
            val status = cursor.getInt(cursor.getColumnIndex(COL_status))
            val videoPath = cursor.getString(cursor.getColumnIndex(COL_video_path))
            downloadData["download_id"] = downloadId
            downloadData["status"] = status
            downloadData["video_path"] = videoPath
        }

        Log.d("Sqlite Data:", "$downloadData")

        cursor.close()
        db.close()
        return downloadData
    }

    @SuppressLint("Range")
    fun deleteMediaFromDB(media_id: String, media_type: String): Int {

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        if (media_type == "series") {
            var query =
                "SELECT $COL_media_id, $COL_season_id FROM $episodes_table WHERE $COL_episode_id = ?"
            val selectionArgs = arrayOf(media_id)
            val cursor = db.rawQuery(query, selectionArgs)
            var tvShowId = ""
            var seasonId = ""
            if (cursor.moveToFirst()) {
                tvShowId = cursor.getString(cursor.getColumnIndex(COL_media_id))
                seasonId = cursor.getString(cursor.getColumnIndex(COL_season_id))
            }
            /////////////
            val whereClause = "$COL_episode_id = ?"
            val whereArgs = arrayOf(media_id)
            db.delete(episodes_table, whereClause, whereArgs)
            /////////////
            if (hasMoreEpisodeRow(tvShowId, seasonId) <= 0) {
                db.delete(
                    seasons_table,
                    "$COL_media_id = ? and $COL_season_id",
                    arrayOf(tvShowId, seasonId)
                )
            }

            if (hasMoreSeasonRow(tvShowId) <= 0) {
                db.delete(main_table, "$COL_media_id = ?", arrayOf(tvShowId))
            }

            return 1

        } else {
            val whereClause = "$COL_media_id = ?"
            val whereArgs = arrayOf(media_id)
            db.delete(main_table, whereClause, whereArgs)
            return 1
        }
    }

    private fun hasMoreEpisodeRow(tv_show_id: String, season_id: String): Int {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        val table = episodes_table
        val columns = arrayOf("COUNT(*)")
        //AND $COL_season_id = ?
        val selection = "$COL_media_id = ? AND $COL_season_id"
        val selectionArgs = arrayOf(tv_show_id, season_id)
        val cursor = db.query(table, columns, selection, selectionArgs, null, null, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }
    private fun hasMoreSeasonRow(tv_show_id: String): Int {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        val table = seasons_table
        val columns = arrayOf("COUNT(*)")
        val selection = "$COL_media_id = ?"
        val selectionArgs = arrayOf(tv_show_id)
        val cursor = db.query(table, columns, selection, selectionArgs, null, null, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }
}