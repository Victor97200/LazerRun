package com.example.lazerrun

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper(context: Context): SQLiteOpenHelper(context, "lazerrun.db", null, 5) { // Increment version to 5
    private val SQL_CREATE_ENTRIES = """
        CREATE TABLE summaries (
            id TEXT PRIMARY KEY,
            categoryId TEXT,
            totalTime LONG,
            runTime LONG,
            avgSpeed DOUBLE,
            minShootingTime LONG,
            avgShootingTime LONG,
            maxShootingTime LONG,
            missedShots INTEGER,
            shootingTime LONG,
            startDateTime TEXT,
            locations TEXT
        )
    """
    private val SQL_ADD_LOCATIONS_COLUMN = "ALTER TABLE summaries ADD COLUMN locations TEXT"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 5) {
            db.execSQL(SQL_ADD_LOCATIONS_COLUMN)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertSummary(summary: Summary) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", summary.id)
            put("categoryId", summary.categoryId)
            put("totalTime", summary.totalTime)
            put("runTime", summary.runTime)
            put("avgSpeed", summary.avgSpeed)
            put("minShootingTime", summary.minShootingTime)
            put("avgShootingTime", summary.avgShootingTime)
            put("maxShootingTime", summary.maxShootingTime)
            put("missedShots", summary.missedShots)
            put("shootingTime", summary.shootingTime)
            put("startDateTime", summary.startDateTime)
            put("locations", summary.locations)
        }
        db.insert("summaries", null, values)
    }

    fun getAllSummaries(): List<Summary> {
        val db = this.readableDatabase
        val cursor = db.query("summaries", null, null, null, null, null, null)
        val summaries = mutableListOf<Summary>()
        with(cursor) {
            while (moveToNext()) {
                val summary = Summary(
                    id = getString(getColumnIndexOrThrow("id")),
                    categoryId = getString(getColumnIndexOrThrow("categoryId")),
                    totalTime = getLong(getColumnIndexOrThrow("totalTime")),
                    runTime = getLong(getColumnIndexOrThrow("runTime")),
                    avgSpeed = getDouble(getColumnIndexOrThrow("avgSpeed")),
                    minShootingTime = getLong(getColumnIndexOrThrow("minShootingTime")),
                    avgShootingTime = getLong(getColumnIndexOrThrow("avgShootingTime")),
                    maxShootingTime = getLong(getColumnIndexOrThrow("maxShootingTime")),
                    missedShots = getInt(getColumnIndexOrThrow("missedShots")),
                    shootingTime = getLong(getColumnIndexOrThrow("shootingTime")),
                    startDateTime = getString(getColumnIndexOrThrow("startDateTime")),
                    locations = getString(getColumnIndexOrThrow("locations"))
                )
                summaries.add(summary)
            }
        }
        cursor.close()
        return summaries
    }

    fun getSummaryById(id: String): Summary? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM summaries WHERE id = ?", arrayOf(id))

        return if (cursor.moveToFirst()) {
            val summary = Summary(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                totalTime = cursor.getLong(cursor.getColumnIndexOrThrow("totalTime")),
                runTime = cursor.getLong(cursor.getColumnIndexOrThrow("runTime")),
                shootingTime = cursor.getLong(cursor.getColumnIndexOrThrow("shootingTime")),
                avgSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow("avgSpeed")),
                minShootingTime = cursor.getLong(cursor.getColumnIndexOrThrow("minShootingTime")),
                avgShootingTime = cursor.getLong(cursor.getColumnIndexOrThrow("avgShootingTime")),
                maxShootingTime = cursor.getLong(cursor.getColumnIndexOrThrow("maxShootingTime")),
                missedShots = cursor.getInt(cursor.getColumnIndexOrThrow("missedShots")),
                categoryId = cursor.getString(cursor.getColumnIndexOrThrow("categoryId")),
                startDateTime = cursor.getString(cursor.getColumnIndexOrThrow("startDateTime")),
                locations = cursor.getString(cursor.getColumnIndexOrThrow("locations"))
            )
            cursor.close()
            summary
        } else {
            cursor.close()
            null
        }
    }
    fun clearSummaries() {
        val db = writableDatabase
        db.delete("summaries", null, null)
    }
}