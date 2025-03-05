package com.example.lazerrun

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Summary(
    val id: String,
    val name: String,
    val initialDistance: Int,
    val lapDistance: Int,
    val lapCount: Int,
    val tireValide: Int,
    val shootDistance: Int,
    val moyennetireValide: Float,
    val tempsTotal: Long,
    val tempsMoyen: Long
)

class SQLHelper(context: Context): SQLiteOpenHelper(context, "lazerrun.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE categories (id TEXT PRIMARY KEY, name TEXT, initialDistance INTEGER, lapDistance INTEGER, lapCount INTEGER, tireValide INTEGER, shootDistance INTEGER, moyennetireValide FLOAT, tempsTotal TIME, tempsMoyen TIME)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        onCreate(db)
    }

    fun insertSummary(id: String, name: String, initialDistance: Int, lapDistance: Int, lapCount: Int, tireValide: Int, shootDistance: Int, moyennetireValide: Float, tempsTotal: Long, tempsMoyen: Long) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
            put("initialDistance", initialDistance)
            put("lapDistance", lapDistance)
            put("lapCount", lapCount)
            put("tireValide", tireValide)
            put("shootDistance", shootDistance)
            put("moyennetireValide", moyennetireValide)
            put("tempsTotal", tempsTotal)
            put("tempsMoyen", tempsMoyen)
        }
        db.insert("categories", null, values)
    }

    fun getAllSummaries(): List<Summary> {
        val db = this.readableDatabase
        val cursor = db.query("categories", null, null, null, null, null, null)
        val summaries = mutableListOf<Summary>()
        with(cursor) {
            while (moveToNext()) {
                val summary = Summary(
                    id = getString(getColumnIndexOrThrow("id")),
                    name = getString(getColumnIndexOrThrow("name")),
                    initialDistance = getInt(getColumnIndexOrThrow("initialDistance")),
                    lapDistance = getInt(getColumnIndexOrThrow("lapDistance")),
                    lapCount = getInt(getColumnIndexOrThrow("lapCount")),
                    tireValide = getInt(getColumnIndexOrThrow("tireValide")),
                    shootDistance = getInt(getColumnIndexOrThrow("shootDistance")),
                    moyennetireValide = getFloat(getColumnIndexOrThrow("moyennetireValide")),
                    tempsTotal = getLong(getColumnIndexOrThrow("tempsTotal")),
                    tempsMoyen = getLong(getColumnIndexOrThrow("tempsMoyen"))
                )
                summaries.add(summary)
            }
        }
        cursor.close()
        return summaries
    }
}