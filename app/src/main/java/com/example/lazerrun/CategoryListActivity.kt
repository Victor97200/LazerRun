package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryListActivity : AppCompatActivity() {
    private lateinit var categoryAdapter: ArrayAdapter<Category>
    private lateinit var service: LaserRunService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        val listView = findViewById<ListView>(R.id.category_list)
        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = categoryAdapter

        // Gestion des clics sur les éléments de la liste
        listView.setOnItemClickListener { _, _, position, _ ->
            val category = categoryAdapter.getItem(position)
            val intent = Intent(this, ChronoActivity::class.java)
            intent.putExtra("totalLapCount", category?.lapCount)
            intent.putExtra("categoryId", category?.id)
            intent.putExtra("lapDistance", category?.lapDistance)
            intent.putExtra("initialDistance", category?.initialDistance)
            startActivity(intent)
        }

        // Initialisation du service Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kahriboo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(LaserRunService::class.java)

        // Récupération des catégories
        fetchCategories()
    }

    private fun fetchCategories() {
        lifecycleScope.launch {
            try {
                val categories = service.getCategories()
                categoryAdapter.clear()
                categoryAdapter.addAll(categories)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}