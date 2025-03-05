package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TireActivity : AppCompatActivity() {
    private var currentLapCount: Int = 1
    private var totalLapCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tire)

        // Handle system bars for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the Chronometer
        val c = findViewById<Chronometer>(R.id.chrono)
        c.base = (application as MyApp).chronoTotal

        // Get the current and total lap count from the intent
        currentLapCount = intent.getIntExtra("currentLapCount", 1)
        totalLapCount = intent.getIntExtra("totalLapCount", 0)

        // Button to navigate back to ChronoActivity
        findViewById<Button>(R.id.button4).setOnClickListener {
            val radioButton1 = findViewById<RadioButton>(R.id.radioButton)
            val radioButton2 = findViewById<RadioButton>(R.id.radioButton2)
            val radioButton3 = findViewById<RadioButton>(R.id.radioButton3)
            val radioButton4 = findViewById<RadioButton>(R.id.radioButton4)
            val radioButton5 = findViewById<RadioButton>(R.id.radioButton5)

            // Update tireValide in MyApp
            val myApp = application as MyApp
            if (radioButton1.isChecked) myApp.tireValide++
            if (radioButton2.isChecked) myApp.tireValide++
            if (radioButton3.isChecked) myApp.tireValide++
            if (radioButton4.isChecked) myApp.tireValide++
            if (radioButton5.isChecked) myApp.tireValide++

            println("Tire valide: ${myApp.tireValide}")

            // Increment the current lap count
            currentLapCount++
                // Pass the updated lap count back to ChronoActivity
                val intent = Intent(this, ChronoActivity::class.java)
                intent.putExtra("currentLapCount", currentLapCount)
                intent.putExtra("totalLapCount", totalLapCount)
                startActivity(intent)
            }
        }
    }