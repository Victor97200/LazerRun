package com.example.lazerrun

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.UUID

class ChronoActivity : AppCompatActivity() {
    private var currentLapCount: Int = 0
    private var totalLapCount: Int = 0
    private var initialDistance: Int = 0
    private var lapDistance: Int = 0
    private var shootDistance: Int = 0
    private var missedShots: Int = 0
    private lateinit var dbHelper: SQLHelper
    private lateinit var categoryId: String
    private var shootingTimes = mutableListOf<Long>()
    private var runTimes = mutableListOf<Long>()
    private lateinit var c3: Chronometer
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chrono)

        dbHelper = SQLHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getCurrentLocation()
        }

        val c = findViewById<Chronometer>(R.id.chrono)
        val myApp = application as MyApp
        if (myApp.chronoBase == 0L) {
            myApp.chronoBase = SystemClock.elapsedRealtime()
        }
        c.base = myApp.chronoBase
        c.start()

        c3 = findViewById(R.id.chrono_Run)
        c3.base = SystemClock.elapsedRealtime()
        c3.start()

        myApp.debutCourse = System.currentTimeMillis().toString()

        currentLapCount = intent.getIntExtra("currentLapCount", 0)
        totalLapCount = intent.getIntExtra("totalLapCount", 0)
        initialDistance = intent.getIntExtra("initialDistance", 0)
        lapDistance = intent.getIntExtra("lapDistance", 0)
        shootDistance = intent.getIntExtra("shootDistance", 0)
        shootingTimes = intent.getLongArrayExtra("shootingTimes")?.toMutableList() ?: mutableListOf()
        missedShots = intent.getIntExtra("missedShots", 0)
        categoryId = intent.getStringExtra("categoryId") ?: ""
        initialDistance = intent.getIntExtra("initialDistance", 0)
        lapDistance = intent.getIntExtra("lapDistance", 0)

        Log.d("ChronoActivity", "initial distance: $initialDistance")

        updateLapCountTextView()

        val actionButton = findViewById<Button>(R.id.button3)

        if (currentLapCount >= totalLapCount) {
            actionButton.text = "Fin de course"
            actionButton.setOnClickListener {
                val runTime = SystemClock.elapsedRealtime() - c3.base
                runTimes.add(runTime)
                myApp.runTimes.add(runTime)
                Log.d("ChronoActivity", "runTimes: ${runTimes.sum()}")

                val summaryId = saveData()
                val intent = Intent(this, SummaryActivity::class.java).apply {
                    putExtra("summaryId", summaryId)
                }
                startActivity(intent)
                finish()
            }
        } else {
            actionButton.text = "Tire"
            actionButton.setOnClickListener {
                val runTime = SystemClock.elapsedRealtime() - c3.base
                runTimes.add(runTime)
                myApp.runTimes.add(runTime)
                Log.d("ChronoActivity", "runTimes: ${runTimes.sum()}")
                val intent = Intent(this, TireActivity::class.java)
                intent.putExtra("currentLapCount", currentLapCount)
                intent.putExtra("totalLapCount", totalLapCount)
                intent.putExtra("shootingTimes", shootingTimes.toLongArray())
                intent.putExtra("missedShots", missedShots)
                intent.putExtra("cumulativeShootingTime", shootingTimes.sum())
                intent.putExtra("categoryId", categoryId)
                intent.putExtra("initialDistance", initialDistance)
                intent.putExtra("lapDistance", lapDistance)
                startActivity(intent)
            }
        }
    }

    private fun updateLapCountTextView() {
        val lapCountTextView = findViewById<TextView>(R.id.textView2)
        lapCountTextView.text = "Lap $currentLapCount/$totalLapCount"
    }

    private fun getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = it
                        Log.d("ChronoActivity", "Location: ${it.latitude}, ${it.longitude}")
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("ChronoActivity", "Location permission not granted", e)
        }
    }

    private fun saveData(): String {
        val totalTime = SystemClock.elapsedRealtime() - (application as MyApp).chronoBase
        val uniqueId = UUID.randomUUID().toString()
        val avgShootingTime = if (shootingTimes.isNotEmpty()) shootingTimes.average().toLong() else 0L
        val minShootingTime = shootingTimes.minOrNull() ?: 0L
        val maxShootingTime = shootingTimes.maxOrNull() ?: 0L
        val totalRunTime = runTimes.sum()
        val totalDistance = initialDistance + lapDistance * totalLapCount
        val avgSpeed = totalDistance / (totalRunTime / 1000.0)
        val shootingTime = shootingTimes.sum()

        val locationString = currentLocation?.let { "${it.latitude},${it.longitude}" } ?: ""

        dbHelper.insertSummary(
            Summary(
                id = uniqueId,
                categoryId = categoryId,
                totalTime = totalTime,
                runTime = totalRunTime,
                avgSpeed = avgSpeed,
                minShootingTime = minShootingTime,
                avgShootingTime = avgShootingTime,
                maxShootingTime = maxShootingTime,
                missedShots = missedShots,
                shootingTime = shootingTime,
                startDateTime = (application as MyApp).debutCourse,
                locations = locationString
            )
        )
        return uniqueId
    }
}