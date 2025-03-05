package com.example.lazerrun

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        dbHelper = SQLHelper(this)
        displaySummary()
    }

    private fun displaySummary() {
        val summaries = dbHelper.getAllSummaries()

        val totalTime = summaries.sumOf { it.tempsTotal }
        val runTime = summaries.sumOf { it.lapDistance }
        val avgSpeed = if (runTime > 0) {
            val distance = 1000 * summaries.size // Example distance in meters
            distance / (runTime / 1000.0) // Convert runTime to seconds
        } else {
            0.0
        }
        val shootingTimes = summaries.map { it.moyennetireValide }
        val minShootingTime = shootingTimes.minOrNull() ?: 0f
        val maxShootingTime = shootingTimes.maxOrNull() ?: 0f
        val avgShootingTime = if (shootingTimes.isNotEmpty()) shootingTimes.average() else 0.0
        val missedShots = summaries.sumOf { it.tireValide }

        findViewById<TextView>(R.id.total_time).text = "Total Time: $totalTime ms"
        findViewById<TextView>(R.id.run_time).text = "Run Time: $runTime ms"
        findViewById<TextView>(R.id.avg_speed).text = "Average Speed: $avgSpeed ms/lap"
        findViewById<TextView>(R.id.min_shooting_time).text = "Min Shooting Time: $minShootingTime ms"
        findViewById<TextView>(R.id.avg_shooting_time).text = "Avg Shooting Time: $avgShootingTime ms"
        findViewById<TextView>(R.id.max_shooting_time).text = "Max Shooting Time: $maxShootingTime ms"
        findViewById<TextView>(R.id.missed_shots).text = "Missed Shots: $missedShots"
    }
}