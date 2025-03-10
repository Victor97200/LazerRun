package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLHelper
    private lateinit var summaryAdapter: ArrayAdapter<String>
    private lateinit var summaries: List<Summary>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = SQLHelper(this)
        summaries = dbHelper.getAllSummaries()

        val listView = findViewById<ListView>(R.id.history_list)
        val displayFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())

        val formattedDates = summaries.map { summary ->
            val date = Date(summary.startDateTime.toLong())
            displayFormat.format(date)
        }

        summaryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, formattedDates)
        listView.adapter = summaryAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val summaryId = summaries[position].id
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("summaryId", summaryId)
            startActivity(intent)
        }
    }
}