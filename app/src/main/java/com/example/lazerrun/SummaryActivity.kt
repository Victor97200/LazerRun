package com.example.lazerrun

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class SummaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        dbHelper = SQLHelper(this)
        displaySummary()
    }

    private fun displaySummary() {
        val summaryId = intent.getStringExtra("summaryId") ?: return
        val summary = dbHelper.getSummaryById(summaryId) ?: return
        Log.d("SummaryActivity", "summaryId: $summaryId")

        findViewById<TextView>(R.id.total_time).text = "Total Time: ${formatTime(summary.totalTime)}"
        findViewById<TextView>(R.id.run_time).text = "Run Time: ${formatTime(summary.runTime)}"
        findViewById<TextView>(R.id.chrono_tire).text = "Shooting Time: ${formatTime(summary.shootingTime)}"
        findViewById<TextView>(R.id.avg_speed).text = "Average Speed: ${summary.avgSpeed} m/s"
        findViewById<TextView>(R.id.min_shooting_time).text = "Min Shooting Time: ${formatTime(summary.minShootingTime)}"
        findViewById<TextView>(R.id.avg_shooting_time).text = "Avg Shooting Time: ${formatTime(summary.avgShootingTime)}"
        findViewById<TextView>(R.id.max_shooting_time).text = "Max Shooting Time: ${formatTime(summary.maxShootingTime)}"
        findViewById<TextView>(R.id.missed_shots).text = "Missed Shots: ${summary.missedShots}"
    }

    private fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 60000).toInt()
        val seconds = ((timeInMillis % 60000) / 1000).toInt()
        val milliseconds = (timeInMillis % 1000).toInt()
        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
    }
}