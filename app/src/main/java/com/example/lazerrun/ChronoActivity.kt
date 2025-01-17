package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChronoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chrono)

        // Gestion des barres système pour un affichage immersif
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation et démarrage du chronomètre
        val c = findViewById<Chronometer>(R.id.chrono)
        c.base = (application as MyApp).chronoTotal
        c.start()

        // Bouton pour passer à l'activité suivante
        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(this, TireActivity::class.java)
            startActivity(intent)
        }
    }
}
