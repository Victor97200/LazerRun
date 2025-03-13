package com.example.lazerrun

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatsActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLHelper
    private lateinit var missedShotsChart: LineChart
    private lateinit var avgSpeedChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        // Initialisation de la base de données
        dbHelper = SQLHelper(this)
        missedShotsChart = findViewById(R.id.missedShotsChart)
        avgSpeedChart = findViewById(R.id.avgSpeedChart)

        // Récupération des résumés depuis la base de données
        val summaries = dbHelper.getAllSummaries()
        if (summaries.isEmpty()) return

        val missedShotsEntries = mutableListOf<Entry>()
        val avgSpeedEntries = mutableListOf<Entry>()

        // Remplissage des données pour les graphiques
        summaries.forEachIndexed { index, summary ->
            val xValue = index.toFloat() // Utilisation de l'index pour l'axe X
            missedShotsEntries.add(Entry(xValue, summary.missedShots.toFloat()))
            avgSpeedEntries.add(Entry(xValue, summary.avgSpeed.toFloat()))
        }

        // Configuration du graphique des tirs manqués
        val missedShotsDataSet = LineDataSet(missedShotsEntries, "Missed Shots").apply {
            color = Color.RED
            valueTextColor = Color.BLACK
            setCircleColor(Color.RED)
            circleRadius = 5f
        }

        // Configuration du graphique de la vitesse moyenne
        val avgSpeedDataSet = LineDataSet(avgSpeedEntries, "Avg Speed").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            setCircleColor(Color.BLUE)
            circleRadius = 5f
        }

        // Affichage des graphiques
        missedShotsChart.data = LineData(missedShotsDataSet)
        missedShotsChart.invalidate()
        missedShotsChart.description.text = "Missed Shots Over Time"
        missedShotsChart.description.textSize = 12f
        missedShotsChart.legend.apply {
            form = Legend.LegendForm.LINE
            textSize = 12f
        }

        avgSpeedChart.data = LineData(avgSpeedDataSet)
        avgSpeedChart.invalidate()
        avgSpeedChart.description.text = "Avg Speed Over Time"
        avgSpeedChart.description.textSize = 12f
        avgSpeedChart.legend.apply {
            form = Legend.LegendForm.LINE
            textSize = 12f
        }
    }
}