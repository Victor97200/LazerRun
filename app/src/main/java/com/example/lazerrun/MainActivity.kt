package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var service: LaserRunService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kahriboo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(LaserRunService::class.java)

        // Example usage of Retrofit to fetch categories
        lifecycleScope.launch {
            try {
                val categories = service.getCategories()
                // Handle fetched categories
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Link to CategoryListActivity
        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(this, CategoryListActivity::class.java)
            startActivity(intent)
        }

        // Link to ChronoActivity
        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, ChronoActivity::class.java)
            startActivity(intent)
        }
    }
}