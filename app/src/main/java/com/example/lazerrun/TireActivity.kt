package com.example.lazerrun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tire)

        // Pour gérer les changements de padding en fonction de la barre système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation du Chronometer
        val c = findViewById<Chronometer>(R.id.chrono)
        c.base = (application as MyApp).chronoTotal

        // Initialisation du bouton "fin tire"
        findViewById<Button>(R.id.button4).setOnClickListener {
            val radioButton1 = findViewById<RadioButton>(R.id.radioButton)
            val radioButton2 = findViewById<RadioButton>(R.id.radioButton2)
            val radioButton3 = findViewById<RadioButton>(R.id.radioButton3)
            val radioButton4 = findViewById<RadioButton>(R.id.radioButton4)
            val radioButton5 = findViewById<RadioButton>(R.id.radioButton5)

            // Récupérer l'objet `MyApp` pour mettre à jour tireValide
            val myApp = application as MyApp

            // Vérifier quel RadioButton est sélectionné
            if (radioButton1.isChecked) {
                myApp.tireValide++
            } else if (radioButton2.isChecked) {
                myApp.tireValide++
            } else if (radioButton3.isChecked) {
                myApp.tireValide++
            } else if (radioButton4.isChecked) {
                myApp.tireValide++
            } else if (radioButton5.isChecked) {
                myApp.tireValide++
            }

            // Log ou toast pour voir la valeur de tireValide
            println("Tire valide: ${myApp.tireValide}")

            // Passer à l'activité Chrono
            val intent = Intent(this, ChronoActivity::class.java)
            startActivity(intent)
        }
    }
}
