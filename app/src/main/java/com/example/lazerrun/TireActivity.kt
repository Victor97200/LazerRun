package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

class TireActivity : AppCompatActivity() {
    private var currentLapCount: Int = 1
    private var totalLapCount: Int = 0
    private var shootingTimes = mutableListOf<Long>()
    private var missedShots: Int = 0
    private lateinit var categoryId: String
    private var initialDistance: Int = 0
    private var lapDistance: Int = 0
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tire)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val c = findViewById<Chronometer>(R.id.chrono)
        c.base = (application as MyApp).chronoBase
        c.start()

        val c2 = findViewById<Chronometer>(R.id.chrono_tire)
        c2.base = SystemClock.elapsedRealtime()
        c2.start()

        currentLapCount = intent.getIntExtra("currentLapCount", 1)
        totalLapCount = intent.getIntExtra("totalLapCount", 0)
        missedShots = intent.getIntExtra("missedShots", 0)
        categoryId = intent.getStringExtra("categoryId") ?: ""
        shootingTimes = intent.getLongArrayExtra("shootingTimes")?.toMutableList() ?: mutableListOf()
        initialDistance = intent.getIntExtra("initialDistance", 0)
        lapDistance = intent.getIntExtra("lapDistance", 0)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            transitionToChronoActivity()
        }, 50000) // 50 seconds

        findViewById<Button>(R.id.button4).setOnClickListener {
            handleButtonClick()
        }
    }

    private fun handleButtonClick() {
        val myApp = application as MyApp
        val radioButtons = listOf(
            findViewById<RadioButton>(R.id.radioButton),
            findViewById<RadioButton>(R.id.radioButton2),
            findViewById<RadioButton>(R.id.radioButton3),
            findViewById<RadioButton>(R.id.radioButton4),
            findViewById<RadioButton>(R.id.radioButton5)
        )

        for (radioButton in radioButtons) {
            if (radioButton.isChecked) myApp.tireValide++ else missedShots++
        }

        val shootingTime = SystemClock.elapsedRealtime() - findViewById<Chronometer>(R.id.chrono_tire).base
        shootingTimes.add(shootingTime)
        myApp.shootingTimes.add(shootingTime)

        Log.d("TireActivity", "Cumulative Shooting Time: ${shootingTimes.sum()}")
        Log.d("TireActivity", "Shooting Times: $shootingTimes")

        transitionToChronoActivity()
    }

    private fun transitionToChronoActivity() {
        val intent = Intent(this, ChronoActivity::class.java)
        intent.putExtra("currentLapCount", currentLapCount + 1)
        intent.putExtra("totalLapCount", totalLapCount)
        intent.putExtra("shootingTimes", shootingTimes.toLongArray())
        intent.putExtra("missedShots", missedShots)
        intent.putExtra("categoryId", categoryId)
        intent.putExtra("initialDistance", initialDistance)
        intent.putExtra("lapDistance", lapDistance)

        startActivity(intent)
    }
}