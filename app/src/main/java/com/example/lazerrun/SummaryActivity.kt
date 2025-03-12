package com.example.lazerrun

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class SummaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLHelper
    private lateinit var mapView: MapView
    private var location: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        dbHelper = SQLHelper(this)
        val summaryId = intent.getStringExtra("summaryId") ?: return
        val summary = dbHelper.getSummaryById(summaryId) ?: return

        summary.locations.split(",").let {
            if (it.size == 2) {
                location = GeoPoint(it[0].toDouble(), it[1].toDouble())
            }
        }

        mapView = findViewById(R.id.map)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        location?.let {
            val marker = Marker(mapView)
            marker.position = it
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(it)
        }

        displaySummary()
    }

    private fun displaySummary() {
        val summaryId = intent.getStringExtra("summaryId") ?: return
        val summary = dbHelper.getSummaryById(summaryId) ?: return

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