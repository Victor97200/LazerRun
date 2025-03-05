package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChronoActivity : AppCompatActivity() {
    private var currentLapCount: Int = 1
    private var totalLapCount: Int = 0
    private var startTime: Long = 0
    private val arrivalTimes = mutableListOf<Long>()
    private val shootingTimes = mutableListOf<Long>()
    private val successfulShots = mutableListOf<Int>()
    private lateinit var dbHelper: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chrono)

        dbHelper = SQLHelper(this)

        // Handle system bars for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize and start the chronometer
        val c = findViewById<Chronometer>(R.id.chrono)
        c.base = (application as MyApp).chronoTotal
        c.start()
        // Get the current and total lap count from the intent and display it
        currentLapCount = intent.getIntExtra("currentLapCount", 1)
        totalLapCount = intent.getIntExtra("totalLapCount", 0)
        updateLapCountTextView()

        // Button to navigate to TireActivity or end the race
        val actionButton = findViewById<Button>(R.id.button3)
        updateActionButton(actionButton)
    }

    private fun updateLapCountTextView() {
        val lapCountTextView = findViewById<TextView>(R.id.textView2)
        lapCountTextView.text = "Lap $currentLapCount/$totalLapCount"
    }

    private fun updateActionButton(button: Button) {
        if (currentLapCount >= totalLapCount) {
            button.text = "Fin de course"
            button.setOnClickListener {
                // Save final data and navigate to SummaryActivity
                saveData()
                val intent = Intent(this, SummaryActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            button.text = "Tire"
            button.setOnClickListener {
                saveData()
                // Save the current chronometer time in MyApp
                (application as MyApp).chronoTotal = SystemClock.elapsedRealtime() - startTime
                val intent = Intent(this, TireActivity::class.java)
                intent.putExtra("currentLapCount", currentLapCount)
                intent.putExtra("totalLapCount", totalLapCount)
                startActivity(intent)
            }
        }
    }

    private fun saveData() {
        val totalTime = SystemClock.elapsedRealtime() - startTime
        dbHelper.insertSummary(
            id = "summary$currentLapCount", // Unique ID for each lap
            name = "Summary",
            initialDistance = 1000,
            lapDistance = 1000,
            lapCount = currentLapCount,
            tireValide = successfulShots.sum(),
            shootDistance = 100,
            moyennetireValide = if (shootingTimes.isNotEmpty()) shootingTimes.average().toFloat() else 0f,
            tempsTotal = totalTime,
            tempsMoyen = if (shootingTimes.isNotEmpty()) shootingTimes.average().toLong() else 0L
        )
    }
}